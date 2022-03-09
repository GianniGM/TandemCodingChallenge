package com.giannig.tandemlite.userslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.giannig.tandemlite.api.repositories.TandemRepository
import com.giannig.tandemlite.api.dto.TandemUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for [MainComponentActivity]
 */
class TandemViewModel(
    private val tandemRepository: TandemRepository
) : ViewModel() {

    /**
     * Gets the list of users
     */
    val getTandemUsersList: Flow<PagingData<TandemUser>> = tandemRepository
        .getSearchResultStream()
        .flow
        .cachedIn(viewModelScope)

    /**
     * Save a liked user
     */
    fun likeUser(user: TandemUser, liked: Boolean) = viewModelScope.launch {
        tandemRepository.likeUser(user, liked)
    }

}
