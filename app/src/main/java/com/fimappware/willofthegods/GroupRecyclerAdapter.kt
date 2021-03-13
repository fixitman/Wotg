package com.fimappware.willofthegods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.data.Group


class GroupRecyclerAdapter(
private var groups : List<Group>,
private val groupClickHandler : (id : Long) -> Unit
) : RecyclerView.Adapter<GroupRecyclerAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_layout,parent,false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position],groupClickHandler)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    fun setGroups(groups : List<Group>){
        this.groups = groups
        notifyDataSetChanged()
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val name = itemView.findViewById<TextView>(R.id.tv_group_name)
        private val card = itemView.findViewById<CardView>(R.id.card)

        fun bind(group : Group, clickHandler: (id: Long) -> Unit){
            name.text = group.Name
            card.setOnClickListener {
                clickHandler.invoke(group.id)
            }
        }
    }

}