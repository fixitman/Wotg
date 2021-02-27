package com.fimappware.willofthegods

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.data.Group


private const val TAG = "GroupRecyclerAdapter"

class GroupRecyclerAdapter(private var groups : List<Group>, private val listener:OnGroupClickedListener)
    :  RecyclerView.Adapter<GroupRecyclerAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_layout,parent,false)
        return GroupViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${groups.size}")

        return groups.size
    }

    fun setGroups(groups : List<Group>){
        this.groups = groups
        notifyDataSetChanged()
    }

    inner class GroupViewHolder(itemView: View, private val listener : OnGroupClickedListener) : RecyclerView.ViewHolder(itemView){
        private val name = itemView.findViewById<TextView>(R.id.tv_group_name)
        private val card = itemView.findViewById<CardView>(R.id.card)

        fun bind(group : Group){
            name.text = group.Name
            card.setOnClickListener {
                listener.onGroupClicked(group.id)
            }
        }
    }

    interface OnGroupClickedListener{
        fun onGroupClicked(groupId: Long)
    }

}