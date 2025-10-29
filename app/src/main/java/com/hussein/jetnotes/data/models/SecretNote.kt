package com.hussein.jetnotes.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hussein.jetnotes.domain.models.Notable

@Entity
data class SecretNote(
    @PrimaryKey(autoGenerate = true)
    override val id: Int,
    override val title: String,
    override val content: String,
    override val date: Long
): Notable