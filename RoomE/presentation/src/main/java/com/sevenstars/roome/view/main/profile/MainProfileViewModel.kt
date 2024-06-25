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

    private val _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState: LiveData<UiState<Unit>> get() = _uiState

    private val _count = MutableLiveData<String>()
    val count: LiveData<String> get() = _count

    private val _preferredGenres = MutableLiveData<List<Genres>>()
    val preferredGenres: LiveData<List<Genres>> get() = _preferredGenres

    private val _mbti = MutableLiveData<String>()
    val mbti: LiveData<String> get() = _mbti

    private val _userStrengths = MutableLiveData<List<Strengths>>()
    val userStrengths: LiveData<List<Strengths>> get() = _userStrengths

    private val _themeImportantFactors = MutableLiveData<List<ImportantFactors>>()
    val themeImportantFactors: LiveData<List<ImportantFactors>> get() = _themeImportantFactors

    private val _horrorThemePosition = MutableLiveData<HorrorThemePositions>()
    val horrorThemePosition: LiveData<HorrorThemePositions> get() = _horrorThemePosition

    private val _hintUsagePreference = MutableLiveData<HintUsagePreferences>()
    val hintUsagePreference: LiveData<HintUsagePreferences> get() = _hintUsagePreference

    private val _deviceLockPreference = MutableLiveData<DeviceLockPreferences>()
    val deviceLockPreference: LiveData<DeviceLockPreferences> get() = _deviceLockPreference

    private val _activity = MutableLiveData<Activities>()
    val activity: LiveData<Activities> get() = _activity

    private val _themeDislikedFactors = MutableLiveData<List<DislikedFactors>>()
    val themeDislikedFactors: LiveData<List<DislikedFactors>> get() = _themeDislikedFactors

    private val _color = MutableLiveData<Colors>()
    val color: LiveData<Colors> get() = _color

    fun fetchData() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            getProfileDataUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess { data ->
                    updateProfileData(data)
                    _uiState.value = UiState.Success(Unit)
                }
                .onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }

    private fun updateProfileData(data: SavedProfileData) {
        _mbti.value = if (data.mbti == "NONE") "-" else data.mbti
        _count.value = data.count
        _preferredGenres.value = data.preferredGenres
        _userStrengths.value = data.userStrengths
        _themeImportantFactors.value = data.themeImportantFactors
        _horrorThemePosition.value = data.horrorThemePosition!!
        _hintUsagePreference.value = data.hintUsagePreference!!
        _deviceLockPreference.value = data.deviceLockPreference!!
        _activity.value = data.activity!!
        _themeDislikedFactors.value = data.themeDislikedFactors
        _color.value = data.color!!
    }
}
