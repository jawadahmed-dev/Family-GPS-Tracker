package com.example.familygpstracker.models

import androidx.room.Embedded
import androidx.room.Relation

data class ChildWithParent (

    @Embedded
    val parent: Parent,

    @Relation(
        parentColumn = "parentId",
        entityColumn = "parentId"
    )
    val childDetail: ChildDetail

)