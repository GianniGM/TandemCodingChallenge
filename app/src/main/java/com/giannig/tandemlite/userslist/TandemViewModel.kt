package com.giannig.tandemlite.userslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.giannig.tandemlite.api.repositories.TandemRepository
import com.giannig.tandemlite.api.dto.TandemUser
import kotlinx.coroutines.flow.*

/**
 * todo add doc
 */
class TandemViewModel(
    tandemRepository: TandemRepository
) : ViewModel() {

    val getUsersMutableState: Flow<PagingData<TandemUser>> = tandemRepository
        .getSearchResultStream()
        .flow
        .cachedIn(viewModelScope)


//    /**
//     * todo add doc
//     */
//    fun getUsersFromApi() {
//        loadUsersJob.cancelIfActive()
//        viewModelScope.launch {
//            tandemRepository.getSearchResultStream()
//                .map {
//                    ViewModelState.ShowUserList(it)
//                }
//                .onStart<ViewModelState> {
//                    emit(ViewModelState.Loading)
//                }
//                .catch {
//                    emit(ViewModelState.ShowErrorMessage())
//                }
//                .collect {
//                    getUsersMutableState.value =  it
//                }
//        }
//    }

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