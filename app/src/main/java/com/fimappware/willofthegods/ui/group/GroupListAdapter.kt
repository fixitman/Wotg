package com.fimappware.willofthegods.ui.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.data.Group


class GroupListAdapter(
private val groupClickHandler : (id : Long) -> Unit
) : ListAdapter<Group, GroupListAdapter.GroupViewHolder>(GroupDiff()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_layout,parent,false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(getItem(position),groupClickHandler)
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

class GroupDiff : DiffUtil.ItemCallback<Group>(){

    override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem.Name == newItem.Name &&
                oldItem.Type == newItem.Type

    }

}