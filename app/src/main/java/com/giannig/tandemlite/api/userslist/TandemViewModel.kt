package com.giannig.tandemlite.api.userslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giannig.tandemlite.api.repositories.TandemApiState
import com.giannig.tandemlite.api.repositories.TandemRepository
import com.giannig.tandemlite.api.dto.TandemUser
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * todo add doc
 */
class TandemViewModel(
    private val tandemRepository: TandemRepository
) : ViewModel() {

    private val _state = MutableLiveData<ViewModelState>()
    val state: LiveData<ViewModelState>
        get() = _state

    /**
     * todo add doc
     */
    fun getUsersFromApi() = viewModelScope.launch {
        tandemRepository.getTandemUsers()
            .onStart {
                ViewModelState.Loading
            }
            .map {state ->
                when(state){
                    is TandemApiState.Error -> ViewModelState.ShowErrorMessage(state.errorType)
                    is TandemApiState.Success -> returnUserList(state.users)
                }
            }
            .collect {
              _state.value =  it
            }
    }

    private fun returnUserList(tandemUser: List<TandemUser>): ViewModelState {
        return if(tandemUser.isEmpty()){
            ViewModelState.Empty
        }else{
            ViewModelState.ShowUserList(tandemUser)
        }
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
    data class ShowUserList(val userList: List<TandemUser>) : ViewModelState

    /**
     * todo add doc
     */
    object Empty: ViewModelState
}