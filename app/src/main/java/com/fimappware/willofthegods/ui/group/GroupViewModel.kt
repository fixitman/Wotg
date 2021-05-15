package com.fimappware.willofthegods.ui.group

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.Group
import kotlinx.coroutines.launch

private const val TAG = "GroupViewModel"

class GroupViewModel(private val appDb: AppDb) : ViewModel() {

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


    class Factory(private val appDb: AppDb) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            Log.d(TAG, "creating viewModel")
            return modelClass.getConstructor(AppDb::class.java).newInstance(appDb)
        }
    }
}




