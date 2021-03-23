package com.fimappware.willofthegods

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.data.GroupItem
import com.google.android.material.switchmaterial.SwitchMaterial

class ItemListAdapter(private val vm : ItemListViewModel) : ListAdapter<GroupItem,ItemListAdapter.ViewHolder>(ListItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemText = itemView.findViewById<TextView>(R.id.tv_item_text)
        val switch = itemView.findViewById<SwitchMaterial>(R.id.sw_enabled)

        fun bind(groupItem: GroupItem){
            itemText.text = groupItem.itemText
            switch.isChecked = groupItem.enabled
            switch.setOnCheckedChangeListener { _, isChecked ->
                vm.setItemEnabled(groupItem.id, isChecked)
            }
        }

    }
}

class ListItemDiff() : DiffUtil.ItemCallback<GroupItem>(){
    override fun areItemsTheSame(oldItem: GroupItem, newItem: GroupItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroupItem, newItem: GroupItem): Boolean{
        return oldItem.enabled == newItem.enabled
                && oldItem.groupId == newItem.groupId
//                && oldItem.image.contentEquals( newItem.image)
                && oldItem.itemText == newItem.itemText
    }
}