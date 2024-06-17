package com.sevenstars.roome.view.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.model.profile.info.Activities
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.domain.model.profile.info.DeviceLockPreferences
import com.sevenstars.domain.model.profile.info.DislikedFactors
import com.sevenstars.domain.model.profile.info.Genres
import com.sevenstars.domain.model.profile.info.HintUsagePreferences
import com.sevenstars.domain.model.profile.info.HorrorThemePositions
import com.sevenstars.domain.model.profile.info.ImportantFactors
import com.sevenstars.domain.model.profile.info.Strengths
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainProfileViewModel@Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase
): ViewModel() {

    private var _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState: LiveData<UiState<Unit>> get() = _uiState

    fun fetchData(){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            getProfileDataUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess {
//                    UiState.Success(Unit)
                    with(it){
                        _mbti.value = if(mbti == "NONE") "-" else mbti
                        _count.value = count
                        _preferredGenres.value = preferredGenres
                        _userStrengths.value = userStrengths
                        _themeImportantFactors.value = themeImportantFactors
                        _horrorThemePosition.value = horrorThemePosition!!
                        _hintUsagePreference.value = hintUsagePreference!!
                        _deviceLockPreference.value = deviceLockPreference!!
                        _activity.value = activity!!
                        _themeDislikedFactors.value = themeDislikedFactors
                        _color.value = color!!
                    }
                }
                .onFailure { code, msg ->
                    UiState.Failure(code, msg)
                }
        }
    }

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
}