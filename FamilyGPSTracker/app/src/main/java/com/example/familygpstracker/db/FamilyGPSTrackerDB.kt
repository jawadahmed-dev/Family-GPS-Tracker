package com.example.familygpstracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.familygpstracker.models.Child
import com.example.familygpstracker.models.ChildDetail
import com.example.familygpstracker.models.Parent
import com.example.familygpstracker.models.ParentDetail

@Database(entities = [Parent::class, ParentDetail::class, Child::class, ChildDetail::class], version = 1)
abstract class FamilyGPSTrackerDB : RoomDatabase() {

    abstract fun parentDetailDao() : ParentDetailDao
    abstract fun childDetailDao() : ChildDetailDao

    companion object{
        private var INSTANCE : FamilyGPSTrackerDB? = null

        fun getDatabase(context: Context) : FamilyGPSTrackerDB{

            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    FamilyGPSTrackerDB::class.java,
                "FamilyGPSTrackerDB").build()

            }
            return INSTANCE!!
        }

    }
}