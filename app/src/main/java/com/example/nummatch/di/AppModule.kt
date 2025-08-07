package com.example.nummatch.di

import android.content.Context
import androidx.room.Room
import com.example.nummatch.data.datastore.DataStoreManager
import com.example.nummatch.data.source.GameDataSource
import com.example.nummatch.data.repository.GameRepositoryImpl
import com.example.nummatch.domain.repository.GameRepository
import com.example.nummatch.data.local.ScoreDao
import com.example.nummatch.data.local.ScoreDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext app: Context): ScoreDatabase =
            Room.databaseBuilder(
                app,
                ScoreDatabase::class.java,
                "score_db"
            ).build()

        @Provides
        @Singleton
        fun provideScoreDao(db: ScoreDatabase): ScoreDao = db.scoreDao()

        @Provides
        @Singleton
        fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
            return DataStoreManager(context)
        }

        @Provides
        @Singleton
        fun provideGameDataSource(
            scoreDao: ScoreDao,
            dataStoreManager: DataStoreManager
        ): GameDataSource {
            return GameDataSource(scoreDao, dataStoreManager)
        }
    }

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        gameRepositoryImpl: GameRepositoryImpl
    ): GameRepository
}