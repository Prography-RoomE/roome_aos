package com.sevenstars.roome.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.roome.utils.UiState
import javax.inject.Inject

class ProfileViewModel@Inject constructor(
): ViewModel() {
    private val _profileState = MutableLiveData<UiState<ProfileState>>(UiState.Loading)
    val profileState: LiveData<UiState<ProfileState>> get() = _profileState

    fun fetchData(){
        _profileState.value = UiState.Loading

        _profileState.value = UiState.Success(ProfileState.ONE)
    }
}