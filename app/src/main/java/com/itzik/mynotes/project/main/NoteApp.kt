package com.itzik.mynotes.project.main

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApp : Application() {
    companion object {
        private var instance: NoteApp? = null

        fun getInstance(): Context {
            return instance!!.applicationContext
        }
    }
}