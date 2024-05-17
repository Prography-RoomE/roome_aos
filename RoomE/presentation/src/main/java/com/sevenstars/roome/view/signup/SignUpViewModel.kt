package com.sevenstars.roome.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.user.SaveNickUseCase
import com.sevenstars.domain.usecase.user.SaveTermsAgreementUseCase
import com.sevenstars.domain.usecase.user.ValidationNickUseCase
import com.sevenstars.roome.utils.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val validationNickUseCase: ValidationNickUseCase,
    private val saveTermsAgreementUseCase: SaveTermsAgreementUseCase,
    private val saveNickUseCase: SaveNickUseCase
): ViewModel() {


    // 회원가입 요청 상태
    private val _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState: LiveData<UiState<Unit>> get() = _uiState

    // 닉네임 유효성 체크 상태
    private val _checkState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val checkState: LiveData<UiState<Unit>> get() = _checkState

    fun resetCheckNick() {
        _checkState.value = UiState.Loading
    }

    fun checkNick(){
        viewModelScope.launch {

        }


        _checkState.value = UiState.Success(Unit)
    }

}