package com.example.familygpstracker.db

import androidx.room.Embedded
import androidx.room.Relation
import com.example.familygpstracker.models.Child
import com.example.familygpstracker.models.ParentDetail


data class ParentWithChildren(
    @Embedded val parentDetail: ParentDetail,
    @Relation(
        parentColumn = "parentId",
        entityColumn = "parentId"
    )
    val children: List<Child>
)