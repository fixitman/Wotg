package com.fimappware.willofthegods.ui.group

import android.util.Log
import androidx.lifecycle.*
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.Group
import kotlinx.coroutines.launch

private const val TAG = "GroupViewModel"

class GroupViewModel(private val appDb: AppDb) : ViewModel() {

    private var groups = MutableLiveData<List<Group>>()
    val groupList : LiveData<List<Group>> = groups

    init {
        refreshGroups()
    }

    suspend fun getGroupById(groupId : Long) : Group{
        return appDb.groupDao().getById(groupId)
    }

    fun addGroup(group: Group){
        viewModelScope.launch {
            val id = appDb.groupDao().insert(group)
            if (id > 0) refreshGroups()
        }
    }

    fun deleteGroup(groupId : Long){
        viewModelScope.launch {
            appDb.groupDao().deleteById(groupId)
            refreshGroups()
        }
    }

    private fun refreshGroups(){
        viewModelScope.launch{
            groups.value = appDb.groupDao().getAll()
        }
    }

    class Factory(private val appDb: AppDb) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            Log.d(TAG, "creating viewModel")
            return modelClass.getConstructor(AppDb::class.java).newInstance(appDb)
        }
    }
}




