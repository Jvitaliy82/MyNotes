package com.jdeveloperapps.noteapp.di

import android.content.Context
import androidx.room.Room
import com.jdeveloperapps.noteapp.database.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,
            "note_db"
        ).build()

    @Singleton
    @Provides
    fun provideNoteDao(db: NotesDatabase) = db.getNoteDao()


}