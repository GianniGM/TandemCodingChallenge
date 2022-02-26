package com.giannig.tandemlite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giannig.tandemlite.api.dto.Response
import com.giannig.tandemlite.api.dto.TandemApiDto
import kotlinx.coroutines.flow.catch
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
        tandemRepository.loadTandemUsers()
            .onStart {
                ViewModelState.Loading
            }
            .map {state ->
                when(state){
                    is TandemApiState.Error -> ViewModelState.ShowErrorMessage(state.errorType)
                    is TandemApiState.Success -> returnUserList(state.tandemApiDto.response)
                }
            }
            .collect {
              _state.value =  it
            }
    }

    private fun returnUserList(response: List<Response>): ViewModelState {
        return if(response.isEmpty()){
            ViewModelState.Empty
        }else{
            ViewModelState.ShowUserList(response)
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
    data class ShowUserList(val userList: List<Response>) : ViewModelState

    /**
     * todo add doc
     */
    object Empty: ViewModelState
}