package crabster.rudakov.requestsmediacontent.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import crabster.rudakov.requestsmediacontent.R
import crabster.rudakov.requestsmediacontent.data.MediaData
import crabster.rudakov.requestsmediacontent.databinding.FragmentAttachedFilesBinding
import crabster.rudakov.requestsmediacontent.view.MediaAdapter
import crabster.rudakov.requestsmediacontent.view.viewmodels.AttachedFilesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

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
        val inflater: MenuInflater = activity!!.menuInflater
        inflater.inflate(R.menu.fab_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        attachedFilesViewModel.submitArgument(id = item.itemId)
        attachedFilesViewModel.args?.let {
            MediaFragment.newInstance(it)
            MediaFragment.show(childFragmentManager, it)
        }
        return super.onContextItemSelected(item)
    }

    override fun onItemDataSelected(mediaData: MediaData) {
        attachedFilesViewModel.submitMediaData(mediaData)
    }

}