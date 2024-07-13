package com.sevenstars.roome.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.ProfileInfoEntity
import com.sevenstars.domain.model.profile.SelectedProfileData
import com.sevenstars.domain.usecase.profile.GetProfileDataUseCase
import com.sevenstars.domain.usecase.profile.GetProfileInfoUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase
): ViewModel() {
    var selectedProfileData = SelectedProfileData(
        0, null, listOf(), listOf(), listOf(), listOf(), null, null, null, null, listOf(), null
    )

    fun resetSelectedProfileData(){
        selectedProfileData = SelectedProfileData(
            0, null, listOf(), listOf(), listOf(), listOf(), null, null, null, null, listOf(), null
        )
    }

    private val _profileState = MutableLiveData<UiState<ProfileState>>(UiState.Loading)
    val profileState: LiveData<UiState<ProfileState>> get() = _profileState

    fun fetchSaveData(){
        _profileState.value = UiState.Loading

        viewModelScope.launch {
            getProfileDataUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess {
                    selectedProfileData.run {

                        try {
                            count = it.count.toInt()
                        } catch (e: Exception){
                            countRange = it.count
                        }

                        genres = it.preferredGenres
                        mbti = convertToListOfPairs(it.mbti)
                        strengths = it.userStrengths
                        important = it.themeImportantFactors
                        horror = it.horrorThemePosition
                        hint = it.hintUsagePreference
                        deviceOrLock = it.deviceLockPreference
                        activity = it.activity
                        dislike = it.themeDislikedFactors
                        color = it.color
                    }

                    _profileState.value = UiState.Success(it.state)
                }
                .onFailure { code, msg ->
                    _profileState.value = UiState.Failure(code, msg)
                }
        }
    }

    private fun convertToListOfPairs(input: String): List<Pair<Int, String>> {
        if (input == "NONE" || input.length != 4) {
            return emptyList()
        }

        val charToNumber = mapOf(
            'E' to 1, 'I' to 1,
            'N' to 2, 'S' to 2,
            'T' to 3, 'F' to 3,
            'J' to 4, 'P' to 4
        )

        return input.map { char ->
            val number = charToNumber[char] ?: error("Invalid character: $char")
            Pair(number, char.toString())
        }
    }

    lateinit var profileDefaultData: ProfileInfoEntity
    private var _profileDataState = MutableLiveData<UiState<ProfileState>>(UiState.Loading)
    val profileDataState get() = _profileDataState

    fun fetchDefaultData(profileState: ProfileState){
        _profileDataState.value = UiState.Loading

        viewModelScope.launch {
            getProfileInfoUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess {
                    profileDefaultData = it
                    _profileDataState.value = UiState.Success(profileState)
                }
                .onFailure { code, msg ->
                    _profileDataState.value = UiState.Failure(code, msg)
                }
        }
    }

    private var _updateProfileDataState = MutableLiveData<UiState<ProfileState>>(UiState.Loading)
    val updateProfileDataState get() = _updateProfileDataState

    fun updateProfileData(){
        _updateProfileDataState.value = UiState.Loading

        viewModelScope.launch {
            getProfileDataUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess {
                    selectedProfileData.run {

                        try {
                            count = it.count.toInt()
                        }catch (e: Exception){
                            countRange = it.count
                        }

                        genres = it.preferredGenres
                        mbti = convertToListOfPairs(it.mbti)
                        strengths = it.userStrengths
                        important = it.themeImportantFactors
                        horror = it.horrorThemePosition
                        hint = it.hintUsagePreference
                        deviceOrLock = it.deviceLockPreference
                        activity = it.activity
                        dislike = it.themeDislikedFactors
                        color = it.color
                    }

                    _updateProfileDataState.value = UiState.Success(it.state)
                }
                .onFailure { code, msg ->
                    _updateProfileDataState.value = UiState.Failure(code, msg)
                }
        }
    }
}