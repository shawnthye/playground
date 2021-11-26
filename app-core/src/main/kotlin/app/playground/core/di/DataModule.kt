package app.playground.core.di

import android.content.Context
import androidx.room.Room
import app.playground.core.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "app_database"

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.inMemoryDatabaseBuilder(
        context,
        AppDatabase::class.java,
    ).build()

    // @Singleton
    // @Provides
    // fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room
    //     .databaseBuilder(
    //         context,
    //         AppDatabase::class.java,
    //         DATABASE_NAME,
    //     )
    //     .fallbackToDestructiveMigration()
    //     .build()

    @Singleton
    @Provides
    fun provideDeviationDao(database: AppDatabase) = database.deviationDao()

    @Singleton
    @Provides
    fun provideDeviationTrackDao(database: AppDatabase) = database.deviationTrackDao()
}
