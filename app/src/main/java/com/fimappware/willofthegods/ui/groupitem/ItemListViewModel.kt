package com.fimappware.willofthegods.ui.groupitem

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.GroupItem
import kotlinx.coroutines.launch

class ItemListViewModel(private var groupId : Long, private val db: AppDb) : ViewModel() {

    fun setGroupId(id : Long){
        groupId = id
    }

    fun getGroupName() : LiveData<String>{
        return db.groupDao().getGroupName(groupId)
    }

    fun getItemsInGroup(id : Long) : LiveData<List<GroupItem>>{
        return db.groupItemDao().getAllItemsInGroup(id)
    }

    suspend fun getEnabledItemsInGroup(group : Long) : List<GroupItem>{
        return db.groupItemDao().getEnabledItemsInGroup(group)
    }

    fun getEnabledItemsInGroupLive(group : Long) : LiveData<List<GroupItem>>{
        return db.groupItemDao().getEnabledItemsInGroupLive(group)
    }

    fun setItemEnabled(itemId: Long, enabled: Boolean) {
        viewModelScope.launch {
            db.groupItemDao().setItemEnabled(itemId,enabled)
            Log.d("MFC","id: $itemId enabled: $enabled")
        }
    }

    fun updateItem(item : GroupItem) = viewModelScope.launch{
        db.groupItemDao().update(item)
    }

    fun insertItem(item : GroupItem) = viewModelScope.launch {
        db.groupItemDao().insert(item)
    }

    fun deleteItem(groupItem: GroupItem?) = viewModelScope.launch{
        db.groupItemDao().delete(groupItem)
    }

    class Factory(private var groupId : Long, private val db : AppDb) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Long::class.java, AppDb::class.java).newInstance(groupId,db)
        }
    }
}