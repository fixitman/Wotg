package com.fimappware.willofthegods


import android.app.Application
import androidx.lifecycle.*
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.data.Group
import kotlinx.coroutines.launch

class GroupViewModel(val appDb: AppDb, application: Application) : AndroidViewModel(application) {

    private var groups = MutableLiveData<List<Group>>()
    val groupList : LiveData<List<Group>> = groups

    init {
        viewModelScope.launch{
            groups.value = appDb.groupDao().getAll()
        }
    }

    class Factory(private val appDb: AppDb) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(AppDb::class.java).newInstance(appDb)
        }
    }
}




