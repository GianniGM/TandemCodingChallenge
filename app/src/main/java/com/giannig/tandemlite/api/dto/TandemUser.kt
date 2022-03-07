package com.giannig.tandemlite.api.dto


import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.giannig.tandemlite.api.db.Converters

/**
 * Entity for the tandem user
 *
 * NOTE: right now I covert learns and natives as array of string
 * actually it would have been better to have another Language table
 *
 * data class Language {
 *
 *     @PrimaryKey
 *     val languageId: String
 *
 *     @ColumnInfo(name ="first_name")
 *     val languageName: String
 * }
 *
 * and eventually a Relation class that links the external key of [TandemUser]
 * with the Primary Key of language
 *
 * data class LanguageRelation(
 *  @Embedded
 *  val user: TandemUser,
 *  @Relation(parentColumn = "id", entityColumn = "languageId")
 *     val languages: List<Language> = emptyList()
 *  )
 */

@Keep
@Entity(tableName = "tandem_user")
data class TandemUser(
    @PrimaryKey
    val id: Int, // 1

    @ColumnInfo(name ="first_name")
    val firstName: String, // Tobi

    @ColumnInfo(name ="learns")
    val learns: List<String>,

    @ColumnInfo(name = "natives")
    val natives: List<String>,

    @ColumnInfo(name = "url_image")
    val pictureUrl: String, // https://tandem2019.web.app/img/pic1.png

    @ColumnInfo(name = "reference_cnt")
    val referenceCnt: Int,

    @ColumnInfo(name = "user_liked")
    var liked: Boolean = false,

    @ColumnInfo(name = "topic")
    val topic: String // What's something not many people know about you?
)

