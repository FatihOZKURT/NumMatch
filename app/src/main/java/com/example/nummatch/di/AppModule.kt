package com.example.nummatch.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.nummatch.datasource.DataStoreManager
import com.example.nummatch.room.ScoreDao
import com.example.nummatch.room.ScoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): ScoreDatabase =
        Room.databaseBuilder(
            app,
            ScoreDatabase::class.java,
            "score_db"
        ).build()


    @Provides
    fun provideScoreDao(db: ScoreDatabase): ScoreDao = db.scoreDao()


    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }


}
