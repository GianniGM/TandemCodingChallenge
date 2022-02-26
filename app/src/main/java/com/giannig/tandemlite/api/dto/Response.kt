package com.giannig.tandemlite.api.dto


import androidx.annotation.Keep

@Keep
data class Response(
    val firstName: String, // Tobi
    val id: Int, // 1
    val learns: List<String>,
    val natives: List<String>,
    val pictureUrl: String, // https://tandem2019.web.app/img/pic1.png
    val referenceCnt: Int,
    val topic: String // What's something not many people know about you?
)