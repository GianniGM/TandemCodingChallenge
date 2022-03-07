package com.giannig.tandemlite.userslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.giannig.tandemlite.api.repositories.TandemRepository
import com.giannig.tandemlite.api.dto.TandemUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.tan

/**
 * todo add doc
 */
class TandemViewModel(
    val tandemRepository: TandemRepository
) : ViewModel() {

    val getTandemUsersList: Flow<PagingData<TandemUser>> = tandemRepository
        .getSearchResultStream()
        .flow
        .cachedIn(viewModelScope)

    fun likeUser(userId: TandemUser, liked: Boolean) = viewModelScope.launch {
        tandemRepository.likeUser(userId, liked)
    }

}

/**
 * todo add doc
 */
sealed interface ViewModelState {
    /**
     * todo add doc
     */
    object Loading: ViewModelState

    /**
     * todo add doc
     */
    data class ShowErrorMessage(val errorText: String? = null) : ViewModelState

    /**
     * todo add doc
     */
    data class ShowUserList(val userList: PagingData<TandemUser>) : ViewModelState

    /**
     * todo add doc
     */
    object Empty: ViewModelState
}