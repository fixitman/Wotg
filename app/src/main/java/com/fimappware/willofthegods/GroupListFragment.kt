package com.fimappware.willofthegods

import android.os.Bundle
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


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDb.getInstance(requireContext())
        val factory = GroupViewModel.Factory(db)
        vm = ViewModelProvider(requireActivity(),factory)[GroupViewModel::class.java]
        adapter = GroupRecyclerAdapter(vm.groupList.value ?: emptyList())

        recycler = view.findViewById<RecyclerView>(R.id.grouplist).also{
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }

        vm.groupList.observe(viewLifecycleOwner) {
            adapter.setGroups(it)
        }
    }



}