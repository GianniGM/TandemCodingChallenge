package com.giannig.tandemlite.api

import com.giannig.tandemlite.api.dto.TandemApiDto
import retrofit2.http.GET
import retrofit2.http.Path

//https://tandem2019.web.app/api/community_{page}.json


const val BASE_URL = "https://tandem2019.web.app"

/**
 * Retrofit interface to call users from tandem apis
 */
interface TandemApi {

    @GET("/api/community_{page}.json")
    suspend fun getTandemUsers(@Path("page") page: Int) : TandemApiDto

}