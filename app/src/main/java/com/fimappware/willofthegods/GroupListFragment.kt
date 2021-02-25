package com.fimappware.willofthegods

import android.content.Context
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

private const val TAG = "GroupListFragment"
class GroupListFragment : Fragment() {

    private lateinit var vm : GroupViewModel
    private lateinit var recycler : RecyclerView
    private lateinit var adapter: GroupRecyclerAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val db = AppDb.getInstance(context)
        val factory = GroupViewModel.Factory(db)
        Log.d(TAG, "onAttach: initializing vm")
        vm = activity?. let {
            ViewModelProvider(it,factory)[GroupViewModel::class.java]
        } ?: throw (IllegalStateException("Fragment has null activity"))

        adapter = GroupRecyclerAdapter(vm.groupList.value ?: emptyList())

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById<RecyclerView>(R.id.grouplist)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        vm.groupList.observe(viewLifecycleOwner) {
            adapter.setGroups(it)
        }
    }

}