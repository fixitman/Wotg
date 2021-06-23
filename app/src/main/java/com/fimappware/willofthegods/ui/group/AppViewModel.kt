package com.fimappware.willofthegods.ui.group

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.Group
import com.fimappware.willofthegods.data.GroupItem
import kotlinx.coroutines.launch

private const val TAG = "GroupViewModel"

class AppViewModel(private val appDb: AppDb) : ViewModel() {
//================================================================================================
//  Group stuff

    suspend fun getGroupById(groupId: Long): Group {
        return appDb.groupDao().getById(groupId)
    }

    fun getAllGroups(): LiveData<List<Group>> {
        return appDb.groupDao().getAll()
    }

    fun addGroup(group: Group) {
        viewModelScope.launch {
            val id = appDb.groupDao().insert(group)
        }
    }

    fun deleteGroup(groupId: Long) {
        viewModelScope.launch {
            appDb.groupDao().deleteById(groupId)
        }
    }

    fun updateGroup(group: Group) {
        viewModelScope.launch {
            appDb.groupDao().update(group)
        }
    }
//================================================================================================
//  GroupItem stuff

    private var groupId  = 0L

    fun setGroupId(id : Long){
        groupId = id
    }

    fun getGroupName() : LiveData<String>{
        return appDb.groupDao().getGroupName(groupId)
    }

    fun getItemsInGroup(id : Long) : LiveData<List<GroupItem>>{
        return appDb.groupItemDao().getAllItemsInGroup(id)
    }

    suspend fun getEnabledItemsInGroup(group : Long) : List<GroupItem>{
        return appDb.groupItemDao().getEnabledItemsInGroup(group)
    }

    fun getEnabledItemsInGroupLive(group : Long) : LiveData<List<GroupItem>>{
        return appDb.groupItemDao().getEnabledItemsInGroupLive(group)
    }

    fun setItemEnabled(itemId: Long, enabled: Boolean) {
        viewModelScope.launch {
            appDb.groupItemDao().setItemEnabled(itemId,enabled)
            Log.d("MFC","id: $itemId enabled: $enabled")
        }
    }

    fun updateItem(item : GroupItem) = viewModelScope.launch{
        appDb.groupItemDao().update(item)
    }

    fun insertItem(item : GroupItem) = viewModelScope.launch {
        appDb.groupItemDao().insert(item)
    }

    fun deleteItem(groupItem: GroupItem?) = viewModelScope.launch{
        appDb.groupItemDao().delete(groupItem)
    }

//=================================================================================================
//  Numbers stuff

    var from : Int = 1
    var to : Int = 10



//=================================================================================================
    class Factory(private val appDb: AppDb) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            Log.d(TAG, "creating viewModel")
            return modelClass.getConstructor(AppDb::class.java).newInstance(appDb)
        }
    }
}




