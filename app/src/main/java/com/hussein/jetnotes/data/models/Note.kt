package com.hussein.jetnotes.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.hussein.jetnotes.domain.models.Notable

@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = CASCADE
    )]
)
data class Note(

    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    override val title: String,
    override val content: String,
    override val date: Long = System.currentTimeMillis(),
    val categoryId: Int? =null,
    val color: Int? = null,
    val isPinned: Boolean = false
): Notable

