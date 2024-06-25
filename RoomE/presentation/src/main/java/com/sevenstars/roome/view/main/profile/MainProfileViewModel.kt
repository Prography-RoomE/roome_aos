package com.sevenstars.roome.view.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.model.profile.info.*
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainProfileViewModel @Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<SavedProfileData>>(UiState.Loading)
    val uiState: LiveData<UiState<SavedProfileData>> get() = _uiState

    lateinit var nickname: String

    fun fetchData() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            getProfileDataUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess { data ->
                    nickname = data.nickname
                    _uiState.value = UiState.Success(data)
                }
                .onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }
}
