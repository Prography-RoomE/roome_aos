package com.sevenstars.roome.view.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.model.user.ResponseUserInfoEntity
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.domain.usecase.user.GetUserInfoUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainProfileViewModel @Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<SavedProfileData>>(UiState.Loading)
    val uiState: LiveData<UiState<SavedProfileData>> get() = _uiState

    lateinit var nickname: String
    lateinit var imageUrl: String
    lateinit var savedProfileData: SavedProfileData

    fun fetchData() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            getProfileDataUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess { data ->
                    nickname = data.nickname
                    savedProfileData = data
                    _uiState.value = UiState.Success(data)
                }
                .onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }

    private val _userState = MutableLiveData<UiState<ResponseUserInfoEntity>>(UiState.Loading)
    val userState: LiveData<UiState<ResponseUserInfoEntity>> get() = _userState

    fun fetchUserInfo() {
        _userState.value = UiState.Loading

        viewModelScope.launch {
            getUserInfoUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess { data ->
                    imageUrl = data.imageUrl
                    _userState.value = UiState.Success(data)
                }
                .onFailure { code, msg ->
                    _userState.value = UiState.Failure(code, msg)
                }
        }
    }
}
