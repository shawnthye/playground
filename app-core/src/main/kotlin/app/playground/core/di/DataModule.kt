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
    fun provideDatabase(@ApplicationContext context: Context) = Room
        .inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java,
            // DATABASE_NAME,
        )
        .fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationFrom(0)
        .build()

    @Singleton
    @Provides
    fun provideDeviationDao(database: AppDatabase) = database.deviationDao()
}
