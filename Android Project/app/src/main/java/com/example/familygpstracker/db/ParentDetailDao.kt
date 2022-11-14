package com.example.familygpstracker.db

import androidx.room.*
import com.example.familygpstracker.models.Child
import com.example.familygpstracker.models.ParentDetail

@Dao
interface ParentDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParentDetail(parent : ParentDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertChild(child : Child)

    @Update
    suspend fun updateParentDetail(parent : ParentDetail)
    @Delete
    suspend fun deleteParentDetail(parent : ParentDetail)

    @Transaction
    @Query("SELECT * FROM ParentDetail Where parentId = :id ")
    suspend fun getParentWithChildren(id : String) : ParentWithChildren
}