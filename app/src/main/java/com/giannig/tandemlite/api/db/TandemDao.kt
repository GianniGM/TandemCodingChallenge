package com.giannig.tandemlite.api.db

import androidx.room.*
import com.giannig.tandemlite.api.dto.TandemUser

@Dao
interface TandemDao {

    @Query("SELECT * FROM tandem_user")
    suspend fun getTandemUsers(): List<TandemUser>

    @Insert
    suspend fun insertAll(users: List<TandemUser>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: TandemUser)

    @Delete
    suspend fun deleteUserFromDB(user: TandemUser)

}