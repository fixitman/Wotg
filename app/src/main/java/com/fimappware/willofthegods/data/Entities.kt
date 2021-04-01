package com.fimappware.willofthegods.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Groups")
data class Group(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="_id")
    var id : Long,
    var Name : String,
    var Type : Int = 0
):Parcelable

@Parcelize
@Entity(tableName = "GroupItems")
data class GroupItem(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id")
    val id : Long,
        var groupId : Long,
        var itemText : String?,
        var enabled : Boolean = true,
        var imageURI : String?
): Parcelable

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