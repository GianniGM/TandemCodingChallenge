package com.giannig.tandemlite

import com.giannig.tandemlite.TandemApiState.*
import com.giannig.tandemlite.api.TandemApi
import com.giannig.tandemlite.api.TandemNetworkService
import com.giannig.tandemlite.api.dto.TandemApiDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import okhttp3.internal.http2.ErrorCode


//todo adding paging
//todo adding room
class TandemRepository {

    /**
     * todo add doc
     */
    fun loadTandemUsers(): Flow<TandemApiState> = flow {
        val dto = getUsersFromApi()
        if (dto.errorCode == null) {
            emit(Success(dto))
        } else {
            emit(Error(dto.errorCode, dto.type))
        }
    }.catch {
        emit(Error())
    }

    private suspend fun getUsersFromApi(): TandemApiDto {
        return TandemNetworkService.getTandemUserFromApis()
    }

}

/**
 * todo add doc
 */
sealed interface TandemApiState {

    /**
     * todo add doc
     */
    data class Success(val tandemApiDto: TandemApiDto) : TandemApiState

    /**
     * if errorCode is null then show a generic error message
     */
    data class Error(
        val errorCode: Int? = null,
        val errorType: String? = null
    ) : TandemApiState
}