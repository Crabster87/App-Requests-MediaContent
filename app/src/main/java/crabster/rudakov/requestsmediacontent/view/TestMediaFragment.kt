package crabster.rudakov.requestsmediacontent.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import crabster.rudakov.requestsmediacontent.data.MediaType
import crabster.rudakov.requestsmediacontent.databinding.FragmentMediaBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TestMediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MediaViewModel by viewModels()
    private var mediaAdapter: MediaAdapter? = null

    private val permissionManager =
        registerForActivityResult(
            ActivityResultContracts
                .RequestMultiplePermissions()
        ) { permissions ->
            val granted = !permissions.entries.any { !it.value }
            if (granted) {
                viewModel.getMedia(MediaType.PHOTO)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaAdapter = MediaAdapter(view.context)
        val lm = GridLayoutManager(view.context, 3)
        lm.isSmoothScrollbarEnabled = false
        lm.initialPrefetchItemCount = 10
        binding.recycler.layoutManager = lm
        binding.recycler.apply {
            layoutManager = lm
            itemAnimator = null
            adapter = mediaAdapter
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.items.collectLatest {
                mediaAdapter?.submitList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mediaAdapter?.currentList()?.isEmpty() == true) {
            checkPermissions { permissionManager.launch(it) }
        }
        viewModel.getMedia(MediaType.PHOTO)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mediaAdapter = null
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

}