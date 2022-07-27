package crabster.rudakov.requestsmediacontent.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import crabster.rudakov.requestsmediacontent.R
import crabster.rudakov.requestsmediacontent.databinding.FragmentAttachedFilesBinding
import crabster.rudakov.requestsmediacontent.view.viewmodels.AttachedFilesViewModel
import crabster.rudakov.requestsmediacontent.view.MediaAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttachedFilesFragment : Fragment() {

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

        binding.fabSelectBtn.setOnClickListener {
            registerForContextMenu(it)
        }
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
        val action = AttachedFilesFragmentDirections.actionAttachedFilesFragmentToMediaFragment(
            attachedFilesViewModel.args
        )
        findNavController().navigate(action)
        return super.onContextItemSelected(item)
    }

}