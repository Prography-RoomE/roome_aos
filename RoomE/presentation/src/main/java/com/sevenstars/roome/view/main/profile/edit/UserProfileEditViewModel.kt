package com.sevenstars.roome.view.main.profile.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.user.DeleteUserImageUseCase
import com.sevenstars.domain.usecase.user.PostUserImageUseCase
import com.sevenstars.domain.usecase.user.SaveNickUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileEditViewModel @Inject constructor(
    private val saveNickUseCase: SaveNickUseCase,
    private val postUserImageUseCase: PostUserImageUseCase,
    private val deleteUserImageUseCase: DeleteUserImageUseCase
): ViewModel() {

    var updateImage: String? = null

    private val _checkState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val checkState: LiveData<UiState<Unit>> get() = _checkState

    fun checkNick(nickname: String){
        _checkState.value = UiState.Loading

        viewModelScope.launch {
            saveNickUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty(), nickname)
                .onSuccess {
                    _checkState.value = UiState.Success(Unit)
                }.onFailure { code, message ->
                    _checkState.value = UiState.Failure(code, message)
                }
        }
    }

    private var _postState = MutableLiveData<UiState<String>>(UiState.Loading)
    val postState get() = _postState

    private var _deleteState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val deleteState get() = _deleteState

    fun postUserImage(realPath: String){
        _postState.value = UiState.Loading

        viewModelScope.launch {
            postUserImageUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty(), realPath)
                .onSuccess {
                    _postState.value = UiState.Success(it)
                }
                .onFailure { code, message ->
                    _postState.value = UiState.Failure(code, message)
                }
        }
    }

    fun deleteUserImage(){
        _deleteState.value = UiState.Loading

        viewModelScope.launch {
            deleteUserImageUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess {
                    _deleteState.value = UiState.Success(Unit)
                }
                .onFailure { code, message ->
                    _deleteState.value = UiState.Failure(code, message)
                }
        }
    }
}