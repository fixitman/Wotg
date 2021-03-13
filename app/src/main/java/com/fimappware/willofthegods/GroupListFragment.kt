package com.fimappware.willofthegods

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.Group

//private const val TAG = "GroupListFragment"
class GroupListFragment : Fragment() {

    private lateinit var vm : GroupViewModel
    private lateinit var recycler : RecyclerView
    private lateinit var adapter: GroupListAdapter
    private lateinit var navController: NavController


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val db = AppDb.getInstance(context)
        val factory = GroupViewModel.Factory(db)

        vm = activity?. let {
            ViewModelProvider(it,factory)[GroupViewModel::class.java]
        } ?: throw (IllegalStateException("Fragment has null activity"))

        adapter = GroupListAdapter(groupClickHandler)
        adapter.submitList(vm.groupList.value?: emptyList<Group>())
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
        navController =view.findNavController()

        recycler = view.findViewById(R.id.grouplist)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        vm.groupList.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())  //toMutableList ensures a new instance of the list is sent
        }
    }

    private val groupClickHandler = { id : Long->
        val arguments = bundleOf(ARG_GROUP_ID to id)
        navController.navigate(R.id.action_groupListFragment_to_addEditGroupFragment,arguments)
    }

    companion object{
        const val ARG_GROUP_ID = "GroupId"
    }
}