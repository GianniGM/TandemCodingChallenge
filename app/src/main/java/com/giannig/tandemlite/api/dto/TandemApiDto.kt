package com.giannig.tandemlite.api.dto


import androidx.annotation.Keep

@Keep
data class TandemApiDto(
    val errorCode: String?, // null
    val response: List<TandemUser>,
    val type: String // success
)