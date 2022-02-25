package com.giannig.tandemlite

import com.giannig.tandemlite.api.TandemNetworkService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart


//todo adding paging
//todo adding room
class TandemRepository {

    /**
     * todo add doc
     */
    fun loadTandemUsers() = flow {
        emit(getUsersFromApi())
    }.catch {
        //todo error handling
    }.onStart {
        //todo loading state
    }

    private suspend fun getUsersFromApi(){
        TandemNetworkService.getTandemUserFromApis()
    }

}