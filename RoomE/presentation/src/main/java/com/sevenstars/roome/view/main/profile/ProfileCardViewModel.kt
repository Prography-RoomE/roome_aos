package com.sevenstars.roome.view.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.model.profile.SavedProfileData
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileCardViewModel@Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase
): ViewModel() {

    private var _uiState = MutableLiveData<UiState<SavedProfileData>>(UiState.Loading)
    val uiState get() = _uiState

    fun fetchSaveData(){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            getProfileDataUseCase.invoke(RoomeApplication.app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }
}