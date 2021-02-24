package com.fimappware.willofthegods.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

const val DB_NAME = "WillOfTheGods.db"
private const val TAG = "AppDb"

private const val DB_VERSION = 1

@Database(entities = [Group::class, GroupItem::class], version = DB_VERSION,exportSchema = true)
abstract class AppDb : RoomDatabase() {

    abstract fun groupDao() : GroupDao
    abstract fun groupItemDao() : GroupItemDao
    


    companion object{
        private var INSTANCE : AppDb? = null

        fun getInstance(context: Context) : AppDb {
            return INSTANCE ?: Room
                .databaseBuilder(context.applicationContext,AppDb::class.java, DB_NAME)
                .addCallback(DB_CALLBACK)
                .build()
                .also {
                    INSTANCE = it
                }
        }

        private val DB_CALLBACK = object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                Log.d(TAG, "onCreate: ")
                super.onCreate(db)
                for( trigger in Triggers.all){
                    db.execSQL(trigger)
                }
            }
        }
    }
}




