package com.fimappware.willofthegods.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroupDao{
    @Query("SELECT * FROM Groups")
    fun getAll() : LiveData<List<Group>>

    @Query("Select name from groups where _id = :id")
    fun getGroupName(id: Long) : LiveData<String>

    @Query("SELECT * FROM GROUPS where _id = :id")
    suspend fun getById(id:Long) : Group

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(group:Group) : Long

    @Update
    suspend fun update(group : Group) : Int

    @Query("DELETE FROM GROUPS WHERE _id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun delete(group: Group)
}



@Dao
interface GroupItemDao{
    @Query("SELECT * FROM GroupItems")
    suspend fun getAll() : List<GroupItem>

    @Query("SELECT * FROM GroupItems where enabled and groupId = :groupId")
    fun getEnabledItemsInGroup(groupId : Long) : LiveData<List<GroupItem>>

    @Query("SELECT * FROM GroupItems WHERE groupId = :groupId")
    fun getAllItemsInGroup(groupId : Long) : LiveData<List<GroupItem>>

    @Query("Select * from GroupItems where _id = :id")
    suspend fun getById(id : Long): GroupItem

    @Update
    suspend fun update(groupItem : GroupItem) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupItem: GroupItem)

    @Query("UPDATE GroupItems SET enabled = :enabled WHERE _id = :itemId")
    suspend fun setItemEnabled(itemId: Long, enabled: Boolean)

    @Delete
    suspend fun delete(groupItem: GroupItem?)
}




