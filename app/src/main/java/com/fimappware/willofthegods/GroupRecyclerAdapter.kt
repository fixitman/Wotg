package com.fimappware.willofthegods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.data.Group

class GroupRecyclerAdapter(private var groups : List<Group>) :  RecyclerView.Adapter<GroupRecyclerAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_layout,parent,false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    fun setGroups(groups : List<Group>){
        this.groups = groups
        notifyDataSetChanged()
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val name = itemView.findViewById<TextView>(R.id.tv_name)

        fun bind(group : Group){
            name.text = group.Name
        }
    }
}