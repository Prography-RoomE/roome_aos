package com.sevenstars.roome.view.splash

import android.os.Debug
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.util.UidVerifier
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.UserState
import com.sevenstars.domain.usecase.common.GetMinUpdateVersionUseCase
import com.sevenstars.domain.usecase.user.GetUserInfoUseCase
import com.sevenstars.roome.BuildConfig
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getMinUpdateVersionUseCase: GetMinUpdateVersionUseCase
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
                    userName = it.nickname
                    isRegister = (it.state == UserState.REGISTRATION_COMPLETED)
                    _loginState.value = UiState.Success(Unit)
                }.onFailure { code, message ->
                    runBlocking(Dispatchers.IO){ app.userPreferences.clearData() }
                    _loginState.value = UiState.Failure(code, message)
                }
        }
    }


    private val _checkState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val checkState: LiveData<UiState<Boolean>> get() = _checkState

    fun checkForceUpdate(){
        _checkState.value = UiState.Loading

        viewModelScope.launch {
            getMinUpdateVersionUseCase.invoke()
                .onSuccess {
                    LoggerUtils.info("강제 업데이트 버전 체크\n현재 버전: ${BuildConfig.VERSION_CODE}\n최소 업데이트 버전: $it")
                    if(it.isEmpty()) _checkState.value = UiState.Success(false)
                    else if (BuildConfig.VERSION_CODE >= it.toInt()) _checkState.value = UiState.Success(false)
                    else _checkState.value = UiState.Success(true)
                }
                .onFailure { code, message ->
                    _checkState.value = UiState.Failure(code, message)
                }
        }
    }
}