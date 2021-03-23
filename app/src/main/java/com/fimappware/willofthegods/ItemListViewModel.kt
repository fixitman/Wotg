package com.fimappware.willofthegods

import androidx.lifecycle.*
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.GroupItem
import kotlinx.coroutines.launch

class ItemListViewModel(private var groupId : Long, private val db: AppDb) : ViewModel() {

    init{
        updateItemList()
    }


    private var _itemList = MutableLiveData<List<GroupItem>>()
    val itemList : LiveData<List<GroupItem>>
        get() = _itemList

    fun updateItemList(){
        viewModelScope.launch {
            _itemList.value = db.groupItemDao().getAllItemsInGroup(groupId)
        }
    }

    fun setItemEnabled(itemId: Long, enabled: Boolean) {
        viewModelScope.launch {
            db.groupItemDao().setItemEnabled(itemId,enabled)
        }

    }


    class Factory(private val db : AppDb) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(AppDb::class.java).newInstance(db)
        }
    }
}