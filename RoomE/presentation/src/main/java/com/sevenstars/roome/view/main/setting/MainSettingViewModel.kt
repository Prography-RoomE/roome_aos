package com.sevenstars.roome.view.main.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.auth.SignOutUseCase
import com.sevenstars.domain.usecase.auth.UnlinkUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSettingViewModel@Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val unlinkUseCase: UnlinkUseCase
): ViewModel() {

    private var _signOutState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val signOutState get() = _signOutState

    private var _unlinkState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val unlinkState get() = _unlinkState

    fun signOut(){
        _signOutState.value = UiState.Loading

        viewModelScope.launch {
            signOutUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess {
                    _signOutState.value = UiState.Success(Unit)
                }
                .onFailure { code, msg ->
                    _signOutState.value = UiState.Failure(code, msg)
                }
        }
    }

    fun unlink(){
        _unlinkState.value = UiState.Loading

        viewModelScope.launch {
            unlinkUseCase.invoke(
                app.userPreferences.getAccessToken().getOrNull().orEmpty(),
                app.userPreferences.getLoginProvider().getOrNull().orEmpty(),
                null
            )
                .onSuccess {
                    _unlinkState.value = UiState.Success(Unit)
                }
                .onFailure { code, msg ->
                    _unlinkState.value = UiState.Failure(code, msg)
                }
        }
    }
}