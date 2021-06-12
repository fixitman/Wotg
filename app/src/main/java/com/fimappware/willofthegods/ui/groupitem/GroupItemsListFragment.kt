package com.fimappware.willofthegods.ui.groupitem

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.SwipeLeftCallback
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.GroupItem
import com.fimappware.willofthegods.databinding.FragmentGroupItemsListBinding
import com.fimappware.willofthegods.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import eltos.simpledialogfragment.form.ColorField
import eltos.simpledialogfragment.form.Input
import eltos.simpledialogfragment.form.SimpleFormDialog
import kotlin.random.Random

private const val TAG = "MFC-GroupItemsListFrag"
class GroupItemsListFragment : Fragment(), ItemListAdapter.CallbackHandler, SimpleDialog.OnDialogResultListener{

    private val vm : ItemListViewModel by lazy{
        val appDb = AppDb.getInstance(requireContext())
        val factory = ItemListViewModel.Factory(groupId, appDb)
        ViewModelProvider(requireActivity(),factory).get(ItemListViewModel::class.java)
    }

    private lateinit var listAdapter : ItemListAdapter
    private lateinit var binding : FragmentGroupItemsListBinding
    private var groupId = 0L
    private var editingItem : GroupItem? = null
    //private var deletedItem : GroupItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = requireArguments()
        groupId = args.getLong(ARG_GROUP_ID, 0L)
        if(groupId == 0L){
            throw java.lang.IllegalArgumentException("No GroupId Supplied")
        }
        listAdapter = ItemListAdapter(this)
        vm.setGroupId(groupId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGroupItemsListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.itemRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        setUpAdapterSwipes()

        vm.getItemsInGroup(groupId).observe(viewLifecycleOwner){
            listAdapter.submitList(it.toMutableList())
        }

        vm.getEnabledItemsInGroup(groupId).observe(viewLifecycleOwner){
            binding.goButton.isEnabled = it.size > 1
        }

        binding.goButton.setOnClickListener {
            onGoClicked()
        }

        binding.groupItemFab.setOnClickListener {
            addItem()
        }
    }

    private fun setUpAdapterSwipes() {

        val deleteSwipeCallback = object : SwipeLeftCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val holder = viewHolder as ItemListAdapter.ViewHolder
                deleteItem(holder.groupItem!!)
            }
        }
        val deleteItemHelper = ItemTouchHelper(deleteSwipeCallback)
        deleteItemHelper.attachToRecyclerView(binding.itemRecyclerView)
    }

    @SuppressLint("ShowToast")
    private fun deleteItem(groupItem: GroupItem) {
        //deletedItem = groupItem
        vm.deleteItem(groupItem)
        Snackbar.make(
            requireView().findViewById(R.id.item_constraint_layout),
            "${groupItem.itemText} deleted",
            Snackbar.LENGTH_LONG
        )
            .setAction("undo") {
                vm.insertItem(groupItem)
            }.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    //deletedItem = null
                }
            }).show()
    }

//    private fun undoDelete(groupItem: GroupItem) {
//        deletedItem?.let {
//            vm.insertItem(it)
//            deletedItem = null
//        }
//    }

    private fun addItem() {
        val colors = resources.getIntArray(SimpleColorDialog.MATERIAL_COLOR_PALLET_LIGHT)
        val color = colors[Random.nextInt(colors.size)]
        SimpleFormDialog.build()
            .title("Edit Item")
            .cancelable(false)
            .pos("OK")
            .neg("CANCEL")
            .fields(
                Input.plain(FIELD_TEXT)
                    .hint("Text")
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .required(),
                ColorField.picker(FIELD_COLOR)
                    .allowCustom(false)
                    .color(color)
                    .label("Color")
                    .colors(requireContext(),SimpleColorDialog.MATERIAL_COLOR_PALLET_LIGHT)
            )
            .show(this,ADD_ITEM_DIALOG)
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
        editItem(groupItem)
    }

    private fun editItem(item: GroupItem){
        editingItem = item
        SimpleFormDialog.build()
            .title("Edit Item")
            .cancelable(false)
            .pos("OK")
            .neg("CANCEL")
            .fields(
                Input.plain(FIELD_TEXT)
                    .hint("Text")
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .text(item.itemText)
                    .required(),
                ColorField.picker(FIELD_COLOR)
                    .allowCustom(false)
                    .color(item.color)
                    .label("Color")
                    .colors(requireContext(),SimpleColorDialog.MATERIAL_COLOR_PALLET_LIGHT)
            )
            .show(this,EDIT_ITEM_DIALOG)
    }

    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        return when(dialogTag){
            EDIT_ITEM_DIALOG -> {
                when(which){
                    SimpleFormDialog.BUTTON_POSITIVE -> {
                        val item = editingItem?.copy(itemText = extras.getString(FIELD_TEXT)
                            ,color = extras.getInt(FIELD_COLOR))
                        vm.updateItem(item!!)
                    }
                }
                editingItem = null
                true
            }
            ADD_ITEM_DIALOG -> {
                when(which){
                    SimpleFormDialog.BUTTON_POSITIVE -> {
                        val item = GroupItem(
                            id = 0L,
                            groupId = groupId,
                            itemText = extras.getString(FIELD_TEXT),
                            color = extras.getInt(FIELD_COLOR)
                        )
                        vm.insertItem(item)
                    }
                }
                true
            }
            else -> false
        }
    }

    companion object {
        const val ARG_GROUP_ID = "GroupId"
        const val FIELD_TEXT = "Text"
        const val FIELD_COLOR = "Color"
        const val EDIT_ITEM_DIALOG = "Edit Item Dialog"
        const val ADD_ITEM_DIALOG = "Add Item Dialog"
    }
}