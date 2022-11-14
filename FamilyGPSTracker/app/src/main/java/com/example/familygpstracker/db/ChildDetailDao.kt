package com.example.familygpstracker.db

import androidx.room.*
import com.example.familygpstracker.models.ChildDetail
import com.example.familygpstracker.models.ChildWithParent
import com.example.familygpstracker.models.Parent

@Dao
interface ChildDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChildDetail(child : ChildDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParent(parent : Parent)

    @Update
    suspend fun updateChild(child : ChildDetail)
    @Delete
    suspend fun deleteChild(child : ChildDetail)

    @Transaction
    @Query("SELECT * FROM ChildDetail Where childId = :id")
    suspend fun getChild(id:String): ChildWithParent
}