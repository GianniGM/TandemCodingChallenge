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
 * todo add doc
 */
class TandemViewModel(
    private val tandemRepository: TandemRepository
) : ViewModel() {

    val getTandemUsersList: Flow<PagingData<TandemUser>> = tandemRepository
        .getSearchResultStream()
        .flow
        .cachedIn(viewModelScope)

    fun likeUser(userId: TandemUser, liked: Boolean) = viewModelScope.launch {
        tandemRepository.likeUser(userId, liked)
    }

}
