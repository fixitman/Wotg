package com.fimappware.willofthegods.ui.groupitem

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.GroupItem
import com.fimappware.willofthegods.databinding.FragmentAddEditItemBinding
import com.fimappware.willofthegods.hideKeyboard
import com.fimappware.willofthegods.ui.MainActivity


class AddEditItemFragment : Fragment() {

    private var item: GroupItem? = null
    private var groupId = 0L
    private var editmode = false

    private lateinit var menu : Menu
    private lateinit var binding : FragmentAddEditItemBinding
    private val vm : ItemListViewModel by lazy{
        val appDb = AppDb.getInstance(requireContext())
        val factory = ItemListViewModel.Factory(groupId, appDb)
        ViewModelProvider(requireActivity(),factory).get(ItemListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(requireArguments()) {
            item = getParcelable(ARG_ITEM)
            groupId = getLong(ARG_GROUP_ID)
        }

        if(item == null){
            item = GroupItem(0L,groupId,"",imageURI = "")
            editmode = false
        }else {
            editmode = true
        }

        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).supportActionBar?.title = if(editmode) "Edit Item" else "Add Item"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save_cancel,menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_cancel -> {
                onCancel()
                return true
            }
            R.id.menu_save -> {
                saveItem()
                return true
            }
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddEditItemBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(editmode){
            binding.itemText.setText(item!!.itemText)
        }

        binding.itemText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                menu.findItem(R.id.menu_save).isEnabled = itemChanged()
            }
        })
    }

    private fun itemChanged() : Boolean{
        return binding.itemText.text.toString() != item!!.itemText
    }

    private fun saveItem(){
        item!!.itemText = binding.itemText.text.toString()
        if(editmode){
            vm.updateItem(item!!)
        }else{
            vm.insertItem(item!!)
        }
        hideKeyboard()
        findNavController().popBackStack()
    }

    private fun onCancel(){
        hideKeyboard()
        findNavController().popBackStack()
    }



    companion object {
        const val ARG_ITEM = "item"
        const val ARG_GROUP_ID = "groupId"
    }
}
