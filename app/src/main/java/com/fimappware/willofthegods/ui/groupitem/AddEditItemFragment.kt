package com.fimappware.willofthegods.ui.groupitem

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.data.GroupItem
import com.fimappware.willofthegods.databinding.FragmentAddEditItemBinding
import com.fimappware.willofthegods.ui.MainActivity


class AddEditItemFragment : Fragment() {

    private var item: GroupItem? = null
    private var groupId = 0L
    private var editmode = false

    private lateinit var binding : FragmentAddEditItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = it.getParcelable<GroupItem>(ARG_ITEM)
            groupId = it.getLong(ARG_GROUP_ID)
        }

        editmode = (item != null)
        Log.d("MFC","Mode = $editmode")

        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).supportActionBar?.title = if(editmode) "Edit Item" else "Add Item"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save_cancel,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_cancel -> {return true}
            R.id.menu_save -> {return true}
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

    }

    companion object {
        const val ARG_ITEM = "item"
        const val ARG_GROUP_ID = "groupId"

    }
}