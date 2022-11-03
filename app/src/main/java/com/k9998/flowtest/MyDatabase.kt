package com.k9998.flowtest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object BatteryModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MyDatabase {
        return Room.databaseBuilder(
            appContext,
            MyDatabase::class.java,
            "my.db"
        ).build()
    }

    @Provides
    fun provideDetectRecordDao(database: MyDatabase): UserDao {
        return database.userDao()
    }

}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BatteryModuleEntryPoint {
    fun userDao(): UserDao
}

@Database(
    entities = [User::class], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}