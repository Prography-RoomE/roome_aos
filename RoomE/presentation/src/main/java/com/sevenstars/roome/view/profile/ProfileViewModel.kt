package com.sevenstars.roome.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.ProfileInfoEntity
import com.sevenstars.domain.usecase.profile.GetProfileInfoUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val getProfileInfoUseCase: GetProfileInfoUseCase
): ViewModel() {
    private val _profileState = MutableLiveData<UiState<ProfileState>>(UiState.Loading)
    val profileState: LiveData<UiState<ProfileState>> get() = _profileState

    fun fetchSaveData(){
        _profileState.value = UiState.Loading

        _profileState.value = UiState.Success(ProfileState.ONE)
    }

    lateinit var profileDefaultData: ProfileInfoEntity
    private var _profileDataState = MutableLiveData<UiState<ProfileState>>(UiState.Loading)
    val profileDataState get() = _profileDataState

    fun fetchData(profileState: ProfileState){
        _profileDataState.value = UiState.Loading

        viewModelScope.launch {
            getProfileInfoUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty())
                .onSuccess {
                    profileDefaultData = it
                    LoggerUtils.info(it.colors.joinToString(","))
                    _profileDataState.value = UiState.Success(profileState)
                }
                .onFailure { code, msg ->
                    _profileDataState.value = UiState.Failure(code, msg)
                }
        }
    }
}