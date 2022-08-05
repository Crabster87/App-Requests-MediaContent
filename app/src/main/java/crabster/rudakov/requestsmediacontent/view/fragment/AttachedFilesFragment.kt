package crabster.rudakov.requestsmediacontent.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import crabster.rudakov.requestsmediacontent.R
import crabster.rudakov.requestsmediacontent.databinding.FragmentAttachedFilesBinding
import crabster.rudakov.requestsmediacontent.view.viewmodel.AttachedFilesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.rusatom.dev.digital.water.media.data.dto.MediaData
import ru.rusatom.dev.digital.water.media.data.dto.MediaType
import ru.rusatom.dev.digital.water.media.view.MediaAdapter
import ru.rusatom.dev.digital.water.media.view.MediaFragment

@AndroidEntryPoint
class AttachedFilesFragment : Fragment(), MediaFragment.MediaDataChoiceListener {

    private var _binding: FragmentAttachedFilesBinding? = null
    private val binding get() = _binding!!
    private var mediaAdapter: MediaAdapter? = null
    private val attachedFilesViewModel: AttachedFilesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttachedFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaAdapter = MediaAdapter()
        val lm = GridLayoutManager(view.context, 3)
        lm.isSmoothScrollbarEnabled = false
        lm.initialPrefetchItemCount = 10
        binding.rvMedia.apply {
            layoutManager = lm
            itemAnimator = null
            adapter = mediaAdapter
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            attachedFilesViewModel.items.collectLatest {
                mediaAdapter?.submitList(it)
            }
        }

        binding.fabSelectBtn.setOnClickListener {
            registerForContextMenu(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mediaAdapter = null
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.apply { inflate(R.menu.fab_menu, menu) }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        val mediaType = when (item.itemId) {
            R.id.action_attach_photo -> MediaType.PHOTO
            R.id.action_attach_video -> MediaType.VIDEO
            else -> null
        }
        launchMediaFragment(mediaType)
        return super.onContextItemSelected(item)
    }

    override fun onItemDataSelected(mediaData: MediaData) {
        attachedFilesViewModel.submitMediaData(mediaData)
    }

    private fun launchMediaFragment(mediaType: MediaType?) {
        MediaFragment.show(childFragmentManager, mediaType)
    }

}