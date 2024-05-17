package com.sevenstars.roome.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.enums.UserState
import com.sevenstars.domain.usecase.user.GetUserInfoUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel() {

    private val _loginState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val loginState: LiveData<UiState<Unit>> get() = _loginState

    var isRegister = false

    fun doValidation(){
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = runBlocking { app.userPreferences.getAccessToken().getOrNull().orEmpty() }

            getUserInfoUseCase(accessToken)
                .onSuccess {
                    isRegister = (it.state == UserState.REGISTRATION_COMPLETED)
                    _loginState.value = UiState.Success(Unit)
                }.onFailure { code, message ->
                    runBlocking(Dispatchers.IO){ app.userPreferences.clearData() }
                    _loginState.value = UiState.Failure(code, message)
                }
        }
    }
}