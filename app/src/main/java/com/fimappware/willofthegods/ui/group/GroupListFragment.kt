package com.fimappware.willofthegods.ui.group

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.snackbar.Snackbar


//private const val TAG = "GroupListFragment"
class GroupListFragment : Fragment(), GroupListAdapter.CallbackHandler {

    //private lateinit var vm: GroupViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: GroupListAdapter
    private lateinit var navController: NavController
    private lateinit var bind: FragmentGroupListBinding
    private var deletedGroup: Group? = null

    private val vm : GroupViewModel by lazy{
        val appDb = AppDb.getInstance(requireContext())
        val factory = GroupViewModel.Factory( appDb)
        ViewModelProvider(requireActivity(),factory).get(GroupViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

//        val db = AppDb.getInstance(context)
//        val factory = GroupViewModel.Factory(db)
//
//        vm = activity?.let {
//            ViewModelProvider(it, factory)[GroupViewModel::class.java]
//        } ?: throw (IllegalStateException("Fragment has null activity"))

       // adapter = GroupListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = FragmentGroupListBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        recycler = bind.grouplist
        recycler.layoutManager = LinearLayoutManager(context)
        adapter = GroupListAdapter(this)
        recycler.adapter = adapter

        setUpAdapterSwipes()

        vm.getAllGroups().observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())  //toMutableList ensures a new instance of the list is sent
            setListVisibility(it.size)
        }
        view.findViewById<FloatingActionButton>(R.id.fab_add_group).setOnClickListener {
            addGroup()
        }
    }

    private fun setUpAdapterSwipes() {
        val editSwipeCallback = object : SwipeRightCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val holder = viewHolder as GroupListAdapter.GroupViewHolder
                holder.group?.let {
                    editGroup(it)
                }
            }
        }
        val editItemHelper = ItemTouchHelper(editSwipeCallback)
        editItemHelper.attachToRecyclerView(recycler)

        val deleteSwipeCallback = object : SwipeLeftCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val holder = viewHolder as GroupListAdapter.GroupViewHolder
                holder.group?.let {
                    deleteGroup(it)
                }
            }
        }
        val deleteItemHelper = ItemTouchHelper(deleteSwipeCallback)
        deleteItemHelper.attachToRecyclerView(recycler)
    }

    private fun setListVisibility(size: Int) {
        if (size == 0) {
            bind.grouplist.visibility = View.GONE
            bind.tvNoGroups.visibility = View.VISIBLE
        } else {
            bind.grouplist.visibility = View.VISIBLE
            bind.tvNoGroups.visibility = View.GONE
        }
    }

    private fun addGroup() {
        InputTextDialog("", "New Group Name", object : InputTextDialog.EventListener {
            override fun onDlgPositiveEvent(dialog: DialogFragment) {
                val text = (dialog as InputTextDialog).getInputText()
                vm.addGroup(Group(0L, text))
            }

            override fun onDlgNegativeEvent(dialog: DialogFragment) {
                dialog.dismiss()
            }
        }).show(childFragmentManager, "addgroupdialog")
    }

    private fun editGroup(group: Group) {
        InputTextDialog(group.Name, "Change Group Name", object : InputTextDialog.EventListener {
            override fun onDlgPositiveEvent(dialog: DialogFragment) {
                val text = (dialog as InputTextDialog).getInputText()
                vm.updateGroup(Group(group.id, text))
            }

            override fun onDlgNegativeEvent(dialog: DialogFragment) {
                dialog.dismiss()
            }
        }).show(childFragmentManager, "editgroupdialog")
    }

    @SuppressLint("ShowToast")
    private fun deleteGroup(group: Group) {
        deletedGroup = group
        vm.deleteGroup(group.id)
        Snackbar.make(
            requireView().findViewById(R.id.groupListConstraintLayout),
            "Group ${group.Name} deleted",
            Snackbar.LENGTH_LONG
        )
            .setAction("undo") {
                undoDelete()
            }.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    deletedGroup = null
                }
            }).show()
    }

    private fun undoDelete() {
        deletedGroup?.let {
            vm.addGroup(it)
            deletedGroup = null
        }
    }

    // required by  GroupListAdapter.CallbackHandler
    override fun groupClicked(id: Long) {
        val arguments = bundleOf(GroupItemsListFragment.ARG_GROUP_ID to id)
        navController.navigate(R.id.action_groupListFragment_to_groupItemsListFragment, arguments)
    }
}