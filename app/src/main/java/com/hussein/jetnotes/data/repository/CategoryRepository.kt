package com.hussein.jetnotes.data.repository

import com.hussein.jetnotes.data.database.CategoryDao
import com.hussein.jetnotes.data.models.Category

class CategoryRepository(private val categoryDao: CategoryDao) {

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    fun getCategories() = categoryDao.getAllCategories()
}