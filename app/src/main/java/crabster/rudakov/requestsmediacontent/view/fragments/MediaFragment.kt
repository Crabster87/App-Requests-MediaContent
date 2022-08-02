package crabster.rudakov.requestsmediacontent.view.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import crabster.rudakov.requestsmediacontent.R
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.data.MediaType
import crabster.rudakov.requestsmediacontent.databinding.FragmentMediaBinding
import crabster.rudakov.requestsmediacontent.view.MediaAdapter
import crabster.rudakov.requestsmediacontent.view.viewmodels.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MediaFragment : DialogFragment() {

    interface MediaDataChoiceListener {
        fun onItemDataSelected(mediaData: MediaData)
    }

    companion object {
        private const val TAG = "DIALOG_MEDIA_TAG"
        private const val ARGS = "MEDIA_TYPE"

        fun newInstance(mediaType: MediaType) =
            MediaFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARGS, mediaType)
                }
            }

        fun show(
            manager: FragmentManager,
            args: MediaType
        ) {
            if (manager.findFragmentByTag(TAG) == null) {
                newInstance(args).show(manager, TAG)
            }
        }
    }

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private val mediaViewModel: MediaViewModel by viewModels()
    private var mediaAdapter: MediaAdapter? = null
    private var mediaDataChoiceListener: MediaDataChoiceListener? = null
    private var args: MediaType? = null

    private val permissionManager =
        registerForActivityResult(
            ActivityResultContracts
                .RequestMultiplePermissions()
        ) { permissions ->
            val granted = !permissions.entries.any { !it.value }
            if (granted) {
                args?.let { mediaViewModel.getMedia(mediaType = it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
        args = arguments?.getParcelable(ARGS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        when {
            parentFragment is MediaDataChoiceListener -> mediaDataChoiceListener =
                parentFragment as MediaDataChoiceListener
            activity is MediaDataChoiceListener -> mediaDataChoiceListener =
                activity as MediaDataChoiceListener
            else -> dismissAllowingStateLoss()
        }

        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaAdapter = MediaAdapter {
            handleOnItemCLick(it)
        }
        val lm = GridLayoutManager(view.context, 3)
        lm.isSmoothScrollbarEnabled = false
        lm.initialPrefetchItemCount = 10
        binding.rvMedia.apply {
            layoutManager = lm
            itemAnimator = null
            adapter = mediaAdapter
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mediaViewModel.items.collectLatest {
                mediaAdapter?.submitList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mediaAdapter?.currentList?.isEmpty() == true) {
            checkPermissions { permissionManager.launch(it) }
        }
        args?.let { mediaViewModel.getMedia(mediaType = it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mediaAdapter = null
        mediaDataChoiceListener = null
    }

    private fun checkPermissions(needPermissions: (permissions: Array<String>) -> Unit): Boolean {
        val permission = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        } else arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_MEDIA_LOCATION
        )

        val result = PermissionChecker.checkSelfPermission(
            requireContext(),
            permission.toString()
        ) == PermissionChecker.PERMISSION_GRANTED
        if (!result) {
            needPermissions(permission)
        } else {
            onStart()
        }
        return result
    }

    private fun handleOnItemCLick(mediaData: MediaData) {
        mediaDataChoiceListener?.onItemDataSelected(mediaData)
        dismiss()
    }

}