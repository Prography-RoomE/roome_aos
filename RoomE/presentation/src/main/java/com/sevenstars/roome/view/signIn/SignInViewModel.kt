package com.sevenstars.roome.view.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.model.auth.RequestSignInEntity
import com.sevenstars.domain.usecase.SignInUseCase
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
): ViewModel() {

    private val _loginState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val loginState: LiveData<UiState<Unit>> get() = _loginState

    fun signIn(provider: String, code: String?, idToken: String?) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            signInUseCase(
                RequestSignInEntity(provider, code, idToken!!)
            ).onSuccess {
                _loginState.value = UiState.Success(Unit)
            }.onFailure {
                _loginState.value = UiState.Failure(it.message)
            }
        }
    }
}