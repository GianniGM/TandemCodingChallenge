package com.giannig.tandemlite.api.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.giannig.tandemlite.api.dto.TandemUser

@Database(entities = [TandemUser::class], version = 1)
abstract class TandemDataBase : RoomDatabase() {
    abstract fun TandemDao(): TandemDao
}


