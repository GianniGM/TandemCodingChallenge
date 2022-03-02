package com.giannig.tandemlite.api.dto


import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "tandem_user")
data class TandemUser(
    @PrimaryKey
    val id: Int, // 1

    @ColumnInfo(name ="first_name")
    val firstName: String, // Tobi

//todo    @ColumnInfo(name ="learns")
//    val learns: List<String>,
//
//    @ColumnInfo(name = "natives")
//    val natives: List<String>,

    @ColumnInfo(name = "url_image")
    val pictureUrl: String, // https://tandem2019.web.app/img/pic1.png

    @ColumnInfo(name = "reference_cnt")
    val referenceCnt: Int,

    @ColumnInfo(name = "topic")
    val topic: String // What's something not many people know about you?
)