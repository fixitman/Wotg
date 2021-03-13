package com.fimappware.willofthegods

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.Group
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

private const val TAG = "MFCAddEditGroupFragment"
class AddEditGroupFragment : Fragment() {
    private var groupId = 0L
    private lateinit var group : Group
    private lateinit var vm : GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{ args ->
            groupId = args.getLong(GroupListFragment.ARG_GROUP_ID, 0L)
        } ?: Log.d(TAG, "OnCreate: no arguments")


//        if(groupId < 1L) throw(IllegalArgumentException("No Group Passed"))
        Log.d(TAG, "onCreate: groupId = $groupId")
        if(groupId < 1L) groupId = 6L

        val db = AppDb.getInstance(requireContext())
        vm = ViewModelProvider(requireActivity(),GroupViewModel.Factory(db))[GroupViewModel::class.java]



    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            group = vm.getGroupById(groupId)
            view.findViewById<Button>(R.id.button_second).text = group.Name
        }
    }


}