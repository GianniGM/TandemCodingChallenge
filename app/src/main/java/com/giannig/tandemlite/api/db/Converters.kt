package com.giannig.tandemlite.api.db

import android.util.Log
import androidx.room.TypeConverter
import com.giannig.tandemlite.api.Moshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import java.lang.reflect.Type

object Converters {

    private val type: Type = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter: JsonAdapter<List<String>> = Moshi.moshi.adapter(type)

    @TypeConverter
    fun fromJsonStringToArray(value: String): List<String> {
        return adapter.fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromArrayToString(date: List<String>): String {
        return adapter.toJson(date)
    }
}