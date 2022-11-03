package com.k9998.flowtest

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    fun queryAll(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE name LIKE '%' || :name || '%'")
    fun queryAll(name: String): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: User): List<Long>

    @Update
    fun update(vararg user: User)

    @Delete
    fun delete(vararg user: User)

}