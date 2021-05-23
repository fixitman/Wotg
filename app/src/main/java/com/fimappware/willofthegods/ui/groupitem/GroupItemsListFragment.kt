package com.fimappware.willofthegods.ui.groupitem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.GroupItem
import com.fimappware.willofthegods.databinding.FragmentGroupItemsListBinding
import com.fimappware.willofthegods.ui.MainActivity

private const val TAG = "MFC-GroupItemsListFrag"
class GroupItemsListFragment : Fragment(), ItemListAdapter.CallbackHandler{

    private val vm : ItemListViewModel by lazy{
        val appDb = AppDb.getInstance(requireContext())
        val factory = ItemListViewModel.Factory(groupId, appDb)
        ViewModelProvider(requireActivity(),factory).get(ItemListViewModel::class.java)
    }


    private lateinit var adapter : ItemListAdapter
    private lateinit var binding : FragmentGroupItemsListBinding
    private var groupId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = requireArguments()
        groupId = args.getLong(ARG_GROUP_ID, 0L)
        if(groupId == 0L){
            throw java.lang.IllegalArgumentException("No GroupId Supplied")
        }
        adapter = ItemListAdapter(this)
        vm.setGroupId(groupId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentGroupItemsListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.itemRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapter
        }

        vm.getItemsInGroup(groupId).observe(viewLifecycleOwner){
            adapter.submitList(it.toMutableList())
        }

        vm.getEnabledItemsInGroup(groupId).observe(viewLifecycleOwner){
            binding.goButton.isEnabled = it.size > 1
        }

        binding.goButton.setOnClickListener {
            onGoClicked()
        }

        binding.groupItemFab.setOnClickListener {
            val args = bundleOf(
                AddEditItemFragment.ARG_ITEM to null
                , AddEditItemFragment.ARG_GROUP_ID to groupId)
            findNavController().navigate(R.id.action_groupItemsListFragment_to_addEditItemFragment,args)
        }
    }

    override fun onResume() {
        super.onResume()
        vm.getGroupName().observe(viewLifecycleOwner){
            (activity as MainActivity).supportActionBar?.title = it
        }
    }

    private fun onGoClicked() {
        //todo : get rid of all this
        Log.d("MFC", "Go Clicked")

    }

    override fun onSwitchClicked(groupItem: GroupItem, isChecked: Boolean) {
        vm.setItemEnabled(groupItem.id, isChecked)
    }

    override fun onItemClicked(groupItem: GroupItem) {
        Log.d("MFC", "Item Clicked : ${groupItem.itemText}")

        val args = bundleOf(AddEditItemFragment.ARG_ITEM to groupItem
            ,AddEditItemFragment.ARG_GROUP_ID to groupId)
        findNavController().navigate(R.id.action_groupItemsListFragment_to_addEditItemFragment,args)
    }

    companion object {
        const val ARG_GROUP_ID = "GroupId"
    }

}