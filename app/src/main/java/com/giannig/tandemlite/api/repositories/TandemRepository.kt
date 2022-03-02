package com.giannig.tandemlite.api.repositories

import com.giannig.tandemlite.api.repositories.TandemApiState.*
import com.giannig.tandemlite.api.TandemNetworkService
import com.giannig.tandemlite.api.db.TandemDao
import com.giannig.tandemlite.api.dto.TandemApiDto
import com.giannig.tandemlite.api.dto.TandemUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


//todo adding paging
//todo adding room
class TandemRepository(
    private val tandemDao: TandemDao
) {

    /**
     * todo
     */
    fun getTandemUsers(withRefresh: Boolean = false): Flow<TandemApiState> = flow {
        val usersFromDb = tandemDao.getTandemUsers()
        if (usersFromDb.isEmpty() || withRefresh) {
            emit(refreshUsers())
        } else {
            emit(Success(usersFromDb))
        }
    }.catch {
        Error()
    }

    private suspend fun refreshUsers(): TandemApiState {
        val dto = getUsersFromApi()
        return if (dto.errorCode == null) {
            tandemDao.insertAll(dto.response)
            Success(dto.response)
        } else {
            Error(dto.errorCode, dto.type)
        }
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
    data class Success(val users: List<TandemUser>) : TandemApiState

    /**
     * if errorCode is null then show a generic error message
     */
    data class Error(
        val errorCode: String? = null,
        val errorType: String? = null
    ) : TandemApiState
}