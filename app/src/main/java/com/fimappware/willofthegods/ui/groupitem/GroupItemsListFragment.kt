package com.fimappware.willofthegods.ui.groupitem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.GroupItem
import com.fimappware.willofthegods.ui.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val TAG = "MFC-GroupItemsListFrag"
class GroupItemsListFragment : Fragment(), ItemListAdapter.CallbackHandler{

    private val vm : ItemListViewModel by lazy{
        val appDb = AppDb.getInstance(requireContext())
        val factory = ItemListViewModel.Factory(groupId, appDb)
        ViewModelProvider(this,factory).get(ItemListViewModel::class.java)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : ItemListAdapter
    private var groupId = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = requireArguments()
        groupId = args.getLong(ARG_GROUP_ID, 0L)
        if(groupId == 0L){
            throw java.lang.IllegalArgumentException("No GroupId Supplied")
        }
        adapter = ItemListAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_items_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.item_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        vm.getItemsInGroup(groupId).observe(viewLifecycleOwner){
            adapter.submitList(it.toMutableList())
        }

        vm.getGroupName().observe(viewLifecycleOwner){
            (activity as MainActivity).supportActionBar?.title = it
        }

        view.findViewById<Button>(R.id.go_button).setOnClickListener {
            onGoClicked()
        }

        view.findViewById<FloatingActionButton>(R.id.group_item_fab).setOnClickListener {
            findNavController().navigate(R.id.action_groupItemsListFragment_to_addEditItemFragment)
        }
    }

    private fun onGoClicked() {
        //todo : get rid of all this
        Log.d("MFC", "Go Clicked")
        val item = GroupItem(0L, 9L, "The Text", true, null)
        val args = bundleOf(AddEditItemFragment.ARG_ITEM to item
            ,AddEditItemFragment.ARG_GROUP_ID to groupId)
        findNavController().navigate(R.id.action_groupItemsListFragment_to_addEditItemFragment,args)
    }

    override fun onSwitchCheckedChange(groupItem: GroupItem, isChecked: Boolean) {
        vm.setItemEnabled(groupItem.id, isChecked)
    }

    override fun onItemClicked(groupItem: GroupItem) {
        Log.d("MFC", "Item Clicked : ${groupItem.itemText}")
    }

    companion object {
        const val ARG_GROUP_ID = "GroupId"
    }

}