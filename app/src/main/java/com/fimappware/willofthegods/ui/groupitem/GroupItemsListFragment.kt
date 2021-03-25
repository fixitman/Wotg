package com.fimappware.willofthegods.ui.groupitem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.GroupItem

private const val TAG = "MFC-GroupItemsListFrag"
class GroupItemsListFragment : Fragment(), ItemListAdapter.CallbackHandler{

    private var groupId: Long = 0L
    private lateinit var vm : ItemListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : ItemListAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupId = it.getLong(ARG_GROUP_ID,0L)
        }
        if(groupId == 0L){
            throw IllegalArgumentException("No group ID supplied")
        }
        val appDb = AppDb.getInstance(requireContext())
        val factory = ItemListViewModel.Factory(groupId, appDb)
        vm = ViewModelProvider(this,factory).get(ItemListViewModel::class.java)
        adapter = ItemListAdapter(this)
        adapter.submitList(vm.itemList.value)
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

        vm.itemList.observe(viewLifecycleOwner){
            adapter.submitList(vm.itemList.value?.toMutableList())
        }

        view.findViewById<Button>(R.id.go_button).setOnClickListener {
            onGoClicked()
        }

    }

    private fun onGoClicked() {
        //todo implement go button click
        Log.d(TAG, "onGoClicked: clicked")
    }

    override fun onSwitchCheckedChange(groupItem: GroupItem, isChecked: Boolean) {
        vm.setItemEnabled(groupItem.id, isChecked)
    }

    companion object {
        const val ARG_GROUP_ID = "GroupId"
    }

}