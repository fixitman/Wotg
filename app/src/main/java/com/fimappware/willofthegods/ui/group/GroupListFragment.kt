package com.fimappware.willofthegods.ui.group

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import com.fimappware.willofthegods.ui.AppViewModel
import com.fimappware.willofthegods.ui.groupitem.GroupItemsListFragment
import com.google.android.material.snackbar.Snackbar
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.input.SimpleInputDialog


//private const val TAG = "GroupListFragment"
class GroupListFragment : Fragment(), GroupListAdapter.CallbackHandler, SimpleDialog.OnDialogResultListener {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: GroupListAdapter
    private lateinit var navController: NavController
    private lateinit var bind: FragmentGroupListBinding
    private var editingGroup : Group? = null


    private val vm : AppViewModel by lazy{
        val appDb = AppDb.getInstance(requireContext())
        val factory = AppViewModel.Factory( appDb)
        ViewModelProvider(requireActivity(),factory).get(AppViewModel::class.java)
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
        bind.fabAddGroup.setOnClickListener {
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
        SimpleInputDialog.build()
            .title(R.string.add_group)
            .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
            .hint(R.string.group_name)
            .cancelable(false)
            .pos(R.string.ok)
            .neg(R.string.cancel)
            .show(this, ADD_GROUP_DIALOG)
    }

    private fun editGroup(group : Group){
        editingGroup = group
        SimpleInputDialog.build()
            .title(R.string.edit_group)
            .inputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
            .hint(R.string.group_name)
            .text(group.Name)
            .neg(R.string.cancel)
            .pos(R.string.ok)
            .cancelable(false)
            .show(this, EDIT_GROUP_DIALOG)
    }

    @SuppressLint("ShowToast")
    private fun deleteGroup(group: Group) {
        vm.deleteGroup(group.id)
        Snackbar.make(
            requireView().findViewById(R.id.groupListConstraintLayout),
            getString(R.string.group_deleted,group.Name),
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.undo) {
                vm.addGroup(group)
            }.show()
    }

    // required by  GroupListAdapter.CallbackHandler
    override fun groupClicked(id: Long) {
        val arguments = bundleOf(GroupItemsListFragment.ARG_GROUP_ID to id)
        navController.navigate(R.id.action_groupListFragment_to_groupItemsListFragment, arguments)
    }

    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        return when(dialogTag){
            ADD_GROUP_DIALOG -> {
                when(which){
                    SimpleDialog.OnDialogResultListener.BUTTON_POSITIVE -> {
                        extras.getString(SimpleInputDialog.TEXT)?.let{
                            vm.addGroup(Group(0L,it))
                        }
                    }
                }
                true
            }

            EDIT_GROUP_DIALOG -> {
                when(which){
                    SimpleDialog.OnDialogResultListener.BUTTON_POSITIVE -> {
                        val text = extras.getString(SimpleInputDialog.TEXT)
                        if(editingGroup?.Name == text){
                            adapter.notifyDataSetChanged() // removes green edit line
                        }else {
                            val id = editingGroup!!.id
                            vm.updateGroup(Group(id, text!!))
                        }
                    }
                    SimpleDialog.OnDialogResultListener.BUTTON_NEGATIVE -> {
                        adapter.notifyDataSetChanged()
                    }
                }
                editingGroup = null
                true
            }
            else -> false
        }
    }

    companion object{
        const val EDIT_GROUP_DIALOG = "edit group dialog"
        const val ADD_GROUP_DIALOG = "add group dialog"
    }
}