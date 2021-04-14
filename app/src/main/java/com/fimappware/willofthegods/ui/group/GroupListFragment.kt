package com.fimappware.willofthegods.ui.group

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.navigation.SwipeRightCallback
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.SwipeLeftCallback
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.Group
import com.fimappware.willofthegods.databinding.FragmentGroupListBinding
import com.fimappware.willofthegods.ui.groupitem.GroupItemsListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


//private const val TAG = "GroupListFragment"
class GroupListFragment : Fragment(), GroupListAdapter.CallbackHandler, InputTextDialog.EventListener {

    private lateinit var vm : GroupViewModel
    private lateinit var recycler : RecyclerView
    private lateinit var adapter: GroupListAdapter
    private lateinit var navController: NavController
    private lateinit var bind : FragmentGroupListBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val db = AppDb.getInstance(context)
        val factory = GroupViewModel.Factory(db)

        vm = activity?. let {
            ViewModelProvider(it,factory)[GroupViewModel::class.java]
        } ?: throw (IllegalStateException("Fragment has null activity"))

        adapter = GroupListAdapter(this)
        adapter.submitList(vm.groupList.value?: emptyList<Group>())

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = FragmentGroupListBinding.inflate(inflater,container,false)
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController =view.findNavController()

        recycler = bind.grouplist
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        val editSwipeCallback = object : SwipeRightCallback(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }
        val editItemHelper = ItemTouchHelper(editSwipeCallback)
        editItemHelper.attachToRecyclerView(recycler)

        val deleteSwipeCallback = object : SwipeLeftCallback(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val holder = viewHolder as GroupListAdapter.GroupViewHolder
                vm.deleteGroup(holder.groupId)
            }
        }
        val deleteItemHelper = ItemTouchHelper(deleteSwipeCallback)
        deleteItemHelper.attachToRecyclerView(recycler)


        vm.groupList.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())  //toMutableList ensures a new instance of the list is sent
            setListVisibility()
        }
        view.findViewById<FloatingActionButton>(R.id.fab_add_group).setOnClickListener {
            addGroup()
        }
        setListVisibility()
    }

    private fun setListVisibility() {
        if (vm.groupList.value.isNullOrEmpty()) {
            bind.grouplist.visibility = View.GONE
            bind.tvNoGroups.visibility = View.VISIBLE
        } else {
            bind.grouplist.visibility = View.VISIBLE
            bind.tvNoGroups.visibility = View.GONE
        }
    }


    override fun groupClicked(id: Long) {
        val arguments = bundleOf(GroupItemsListFragment.ARG_GROUP_ID to id)
        navController.navigate(R.id.action_groupListFragment_to_groupItemsListFragment,arguments)
    }

    private fun addGroup(){
        Toast.makeText(context, "Going to Add/Edit screen", Toast.LENGTH_SHORT).show()
        getGroupName()
    }

    private fun getGroupName(){
        InputTextDialog().show(childFragmentManager,"textinputdialog")
    }

    override fun onDlgPositiveEvent(dialog: DialogFragment) {
        val text = (dialog as InputTextDialog).getInputText()
        vm.addGroup(Group(0L,text))
    }

    override fun onDlgNegativeEvent(dialog: DialogFragment) {
        dialog.dismiss()
    }
}