package com.sevenstars.roome.view.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.enums.Provider
import com.sevenstars.domain.usecase.auth.SignInUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
): ViewModel() {

    private val _loginState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val loginState: LiveData<UiState<Unit>> get() = _loginState

    fun signIn(provider: Provider, code: String?, idToken: String?) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            signInUseCase(provider, code, idToken!!
            ).onSuccess {
                runBlocking(Dispatchers.IO){
                    app.userPreferences.setLoginProvider(provider)
                    app.userPreferences.setAccessToken(it.accessToken)
                    app.userPreferences.setRefreshToken(it.refreshToken)
                }
                _loginState.value = UiState.Success(Unit)
            }.onFailure { code, message ->
                _loginState.value = UiState.Failure(code, message)
            }
        }
    }
}