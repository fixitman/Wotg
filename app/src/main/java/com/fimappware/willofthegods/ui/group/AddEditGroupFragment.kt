package com.fimappware.willofthegods.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.data.AppDb

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

private const val TAG = "MFCAddEditGroupFragment"
class AddEditGroupFragment : Fragment() {

    private lateinit var vm : GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDb.getInstance(requireContext())
        vm = ViewModelProvider(requireActivity(), GroupViewModel.Factory(db))[GroupViewModel::class.java]

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

    }


}