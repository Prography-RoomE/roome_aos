package com.sevenstars.roome.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.repository.auth.UserPreferencesRepository
import com.sevenstars.domain.usecase.user.GetUserInfoUseCase
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _loginState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val loginState: LiveData<UiState<Unit>> get() = _loginState

    fun doValidation(){
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().getOrNull().orEmpty()

            getUserInfoUseCase(accessToken)
                .onSuccess {
                    _loginState.value = UiState.Success(Unit)
                }.onFailure { code, message ->
                    _loginState.value = UiState.Failure(code, message)
                }
        }
    }
}