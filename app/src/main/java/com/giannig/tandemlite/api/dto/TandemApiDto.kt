package com.giannig.tandemlite.api.dto


import androidx.annotation.Keep

@Keep
data class TandemApiDto(
    val response: List<TandemUser>,
    val errorCode: String?, // null
    val type: String // success
)