package com.giannig.tandemlite.api.db

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var INSTANCE: TandemDataBase? = null
    fun getInstance(context: Context): TandemDataBase {
        if (INSTANCE == null) {
            synchronized(TandemDataBase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }
    private fun buildRoomDB(context: Context) = Room.databaseBuilder(
        context.applicationContext,
        TandemDataBase::class.java,
        "tandem-users"
    ).build()
}