package com.sevenstars.roome.view.deeplink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.enums.UserState
import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.model.user.ResponseUserInfoEntity
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.domain.usecase.user.GetUserInfoUseCase
import com.sevenstars.domain.usecase.user.GetUserProfileDataUseCase
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DeepLinkProfileViewModel @Inject constructor(
    private val getUserProfileDataUseCase: GetUserProfileDataUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<SavedProfileData>>(UiState.Loading)
    val uiState: LiveData<UiState<SavedProfileData>> get() = _uiState

    fun fetchData(nickname: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            getUserProfileDataUseCase.invoke(nickname)
                .onSuccess { data ->
                    _uiState.value = UiState.Success(data)
                }
                .onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }

    private val _profileState = MutableLiveData<UiState<SavedProfileData>>(UiState.Loading)
    val profileState: LiveData<UiState<SavedProfileData>> get() = _profileState

    fun doCheckProfileComplete(){
        _profileState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = runBlocking { RoomeApplication.app.userPreferences.getAccessToken().getOrNull().orEmpty() }

            getProfileDataUseCase(accessToken)
                .onSuccess {
                    _profileState.value = UiState.Success(it)
                }.onFailure { code, message ->
                    _profileState.value = UiState.Failure(code, message)
                }
        }
    }
}
