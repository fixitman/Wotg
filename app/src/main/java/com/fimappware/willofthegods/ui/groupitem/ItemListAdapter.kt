package com.fimappware.willofthegods.ui.groupitem

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
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
        fun onSwitchClicked(groupItem: GroupItem, isChecked : Boolean)
        fun onItemClicked(groupItem: GroupItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var groupItem : GroupItem? = null
        val itemText : TextView = itemView.findViewById(R.id.tv_item_text)
        val switch : SwitchMaterial = itemView.findViewById(R.id.sw_enabled)
        var card : CardView = itemView.findViewById(R.id.item_card)



        fun bind(groupItem: GroupItem){
            this.groupItem = groupItem
            itemText.text = groupItem.itemText
            switch.isChecked = groupItem.enabled
            itemView.isEnabled = groupItem.enabled
            itemText.isEnabled = groupItem.enabled
           if(groupItem.enabled){
                card.setCardBackgroundColor(groupItem.color)
            }else{
                card.setCardBackgroundColor(Color.WHITE)
           }

            switch.setOnClickListener {
                val checked = (it as SwitchMaterial).isChecked
                callbackHandler.onSwitchClicked(groupItem, checked)
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
                //&& oldItem.imageURI == newItem.imageURI
                && oldItem.itemText == newItem.itemText
                && oldItem.color == newItem.color
    }
}