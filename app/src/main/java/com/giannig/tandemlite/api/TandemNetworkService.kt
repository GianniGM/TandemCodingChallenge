package com.giannig.tandemlite.api

import com.giannig.tandemlite.api.Moshi.moshi
import com.giannig.tandemlite.api.dto.TandemApiDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Moshi Setup
 */
object Moshi {
    val moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}


/**
 * Service that manages api calls
 */
object TandemNetworkService {

    suspend fun getTandemUserFromApis(page: Int = 1): TandemApiDto {
        return retrofit.getTandemUsers(page)
    }

    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getClient())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(TandemApi::class.java)

}