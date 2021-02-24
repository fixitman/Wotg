package com.fimappware.willofthegods.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Groups")
data class Group(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="_id")
    val id : Long,
    val Name : String,
    val Type : Int = 0
)

@Entity(tableName = "GroupItems")
data class GroupItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id")
    val id : Long,
    val groupId : Long,
    val itemText : String?,
    val enabled : Boolean = true,
    val image : ByteArray?


)

object Triggers{
    val DELETE_GROUP = """
        CREATE TRIGGER deleteGroupTrigger 
            BEFORE DELETE ON Groups
        BEGIN
            DELETE FROM GroupItems where groupId = OLD._id;
        END;
    """.trimIndent()

    

    val all = arrayOf(DELETE_GROUP)
}