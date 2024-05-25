package com.sevenstars.roome.view.profile.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.ProfileInfoEntity
import com.sevenstars.domain.model.profile.SelectedProfileData
import com.sevenstars.domain.repository.ProfileRepository
import com.sevenstars.domain.usecase.profile.DeleteProfileDataUseCase
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.domain.usecase.profile.GetProfileInfoUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileWelcomeViewModel @Inject constructor(
    private val deleteProfileDataUseCase: DeleteProfileDataUseCase
): ViewModel() {

    private val _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState: LiveData<UiState<Unit>> get() = _uiState

    fun deleteProfileData(){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            deleteProfileDataUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess {
                    _uiState.value = UiState.Success(Unit)
                }
                .onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }
}