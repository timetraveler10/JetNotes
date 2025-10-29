package com.hussein.jetnotes.domain.models

interface Notable {
    val id: Int
    val title: String
    val content: String
    val date: Long
}