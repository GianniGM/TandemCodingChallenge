package com.giannig.tandemlite.api.dto


import androidx.annotation.Keep

@Keep
data class TandemApiDto(
    val errorCode: Int?, // null
    val response: List<Response>,
    val type: String // success
)