package com.giannig.tandemlite.api.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.giannig.tandemlite.api.dto.TandemUser

@Database(entities = [TandemUser::class], version = 1)
@TypeConverters(Converters::class)
abstract class TandemDataBase : RoomDatabase() {
    abstract fun TandemDao(): TandemDao
}


