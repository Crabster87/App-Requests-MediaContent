package crabster.rudakov.requestsmediacontent.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import crabster.rudakov.requestsmediacontent.utils.PhotoHelper.saveGeoTag
import java.text.SimpleDateFormat
import java.util.*

class PhotoLocation(private val context: Context) : LocationListener {


    var locationCallBack: ((Location?) -> Unit)? = null
        set(value) {
            field = value
            if (field != null) {
                onStart()
            }
        }
    private var locationManager: LocationManager? = null
        get() {
            if (field == null) {
                locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            }
            return field
        }
    private var criteria: Criteria? = null
        get() {
            if (field == null) {
                criteria = Criteria().apply {
                    accuracy = Criteria.ACCURACY_FINE
                    isCostAllowed = false
                }
            }
            return field
        }
    var location: Location? = null
        set(value) {
            field = value
            locationCallBack?.invoke(field)
        }
        get() {
            founded = field != null
            if (field == null) {
                field = onStart()
            }
            return field
        }


    var dialog: AlertDialog? = null
    private var founded = false

    @SuppressLint("MissingPermission")
    fun onStart(): Location? {
        val provider =
            locationManager!!.getBestProvider(criteria!!, false) ?: LocationManager.PASSIVE_PROVIDER

        var lastLocation: Location? = null
        if (isNetworkEnabled() && !founded) {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
            lastLocation = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        if (isGPSEnabled() && !founded && lastLocation == null) {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
        if (!isNetworkEnabled() && !founded && lastLocation == null) {
            locationManager!!.requestLocationUpdates(provider, 0, 0f, this)
            lastLocation = locationManager!!.getLastKnownLocation(provider)
        }
        return lastLocation
    }

    fun isGPSEnabled(): Boolean {
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun isNetworkEnabled(): Boolean {
        return locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun showDialog(onCancelled: () -> Unit) {
        dialog?.dismiss()
        dialog = AlertDialog.Builder(context)
            .setCancelable(true)
            .setTitle("Открыть настройки")
            .setMessage("Чтобы продолжить, включите на устройстве геолокацию")
            .setNegativeButton(
                "Отмена"
            ) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                onCancelled()
            }
            .setPositiveButton(
                "OK"
            ) { _: DialogInterface, _: Int ->
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }.create()

        dialog?.show()
    }

    fun onStop() {
        locationManager?.removeUpdates(this)
        dialog?.dismiss()
        dialog = null
        locationManager = null
        criteria = null
        location = null
    }

    fun isGeoTagSaved(imagePath: String?): Boolean {
        return saveGeoTag(location, imagePath)
    }

    override fun onLocationChanged(location: Location) {
        this.location = location
    }

    @WorkerThread
    fun createTempFile(intent: (Intent) -> Unit): String? {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = createTempFile("JPEG_${timeStamp}_", ".jpeg", filesDir)
            val photoURI = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            intent(captureIntent)
            file.absolutePath
        } catch (ex: Exception) {
            null
        }
    }
}