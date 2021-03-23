package com.fimappware.willofthegods.data

import androidx.room.*

@Dao
interface GroupDao{
    @Query("SELECT * FROM Groups")
    suspend fun getAll() : List<Group>

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
    suspend fun getEnabledItemsInGroup(groupId : Long) : List<GroupItem>

    @Query("SELECT * FROM GroupItems WHERE groupId = :groupId")
    suspend fun getAllItemsInGroup(groupId : Long) : List<GroupItem>

    @Query("Select * from GroupItems where _id = :id")
    suspend fun getById(id : Long): GroupItem

    @Update
    suspend fun update(groupItem : GroupItem) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupItem: GroupItem)
}




