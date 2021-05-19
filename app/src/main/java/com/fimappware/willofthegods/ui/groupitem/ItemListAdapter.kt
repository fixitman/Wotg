package com.fimappware.willofthegods.ui.groupitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fimappware.willofthegods.R
import com.fimappware.willofthegods.data.GroupItem
import com.google.android.material.switchmaterial.SwitchMaterial

class ItemListAdapter(private val callbackHandler: CallbackHandler) : ListAdapter<GroupItem, ItemListAdapter.ViewHolder>(ListItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface CallbackHandler{
        fun onSwitchCheckedChange(groupItem: GroupItem, isChecked : Boolean)
        fun onItemClicked(groupItem: GroupItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemText = itemView.findViewById<TextView>(R.id.tv_item_text)
        val switch = itemView.findViewById<SwitchMaterial>(R.id.sw_enabled)


        fun bind(groupItem: GroupItem){
            itemText.text = groupItem.itemText
            switch.isChecked = groupItem.enabled

            switch.setOnCheckedChangeListener { _, isChecked ->
                callbackHandler.onSwitchCheckedChange(groupItem, isChecked)
            }
            itemView.setOnClickListener {
                callbackHandler.onItemClicked(groupItem)
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
                && oldItem.imageURI == newItem.imageURI
                && oldItem.itemText == newItem.itemText
    }
}