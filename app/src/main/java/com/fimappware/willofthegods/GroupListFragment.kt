package com.fimappware.willofthegods

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.data.AppDb
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val TAG = "GroupListFragment"
class GroupListFragment : Fragment() {

    private lateinit var vm : GroupViewModel
    private lateinit var recycler : RecyclerView
    private lateinit var adapter: GroupRecyclerAdapter


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_group_list, container, false)
        recycler = view.findViewById<RecyclerView>(R.id.grouplist)
        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm = ViewModelProvider(this, GroupViewModel.Factory(AppDb.getInstance(requireContext()))).get(GroupViewModel::class.java)
        adapter = GroupRecyclerAdapter(vm.groupList.value?: emptyList())
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter


        vm.groupList.observe(viewLifecycleOwner) {
            Log.d(TAG, "onCreate: List changed")
            adapter.setGroups(it)
        }
    }

}