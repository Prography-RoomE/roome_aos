package com.sevenstars.roome.view.main.profile.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.user.SaveNickUseCase
import com.sevenstars.domain.usecase.user.ValidationNickUseCase
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileEditViewModel @Inject constructor(
    private val saveNickUseCase: SaveNickUseCase
): ViewModel() {

    private val _saveState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val saveState: LiveData<UiState<Unit>> get() = _saveState

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
}