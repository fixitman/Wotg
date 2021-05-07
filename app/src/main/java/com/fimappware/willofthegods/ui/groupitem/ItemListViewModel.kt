package com.fimappware.willofthegods.ui.groupitem

import androidx.lifecycle.*
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.GroupItem
import kotlinx.coroutines.launch

class ItemListViewModel(private var groupId : Long, private val db: AppDb) : ViewModel() {


    init{
        updateItemList()
        updateGroupName()
    }

    private var _itemList = MutableLiveData<List<GroupItem>>()
    val itemList : LiveData<List<GroupItem>>
        get() = _itemList

    private var _groupName = MutableLiveData<String>("")
    val groupName : LiveData<String>
        get() = _groupName

    private fun updateItemList(){
        viewModelScope.launch {
            val list = db.groupItemDao().getAllItemsInGroup(groupId)
            _itemList.value = list
        }
    }

    private fun updateGroupName(){
        viewModelScope.launch {
            val gName = db.groupDao().getById(groupId).Name
            _groupName.value = gName
        }
    }

    fun setItemEnabled(itemId: Long, enabled: Boolean) {
        viewModelScope.launch {
            db.groupItemDao().setItemEnabled(itemId,enabled)
            updateItemList()
        }
    }

    class Factory(private var groupId : Long, private val db : AppDb) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Long::class.java, AppDb::class.java).newInstance(groupId,db)
        }
    }
}