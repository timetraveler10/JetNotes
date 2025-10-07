package com.hussein.jetnotes

import android.app.Application
import com.hussein.jetnotes.data.AppContainer
import com.hussein.jetnotes.data.NoteRepository
import com.hussein.jetnotes.data.database.AppDatabase

class JetNotesApp: Application(){
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}