package com.sevenstars.roome.view.profile.important

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.profile.SaveImportantUseCase
import com.sevenstars.domain.usecase.profile.SaveMBTIUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileImportantViewModel @Inject constructor(
    private val saveImportantUseCase: SaveImportantUseCase
): ViewModel() {

    private var _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState get() = _uiState

    fun setLoadingState(){
        _uiState.value = UiState.Loading
    }

    fun saveData(importantFactors: List<Int>){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            saveImportantUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty(), importantFactors)
                .onSuccess {
                    _uiState.value = UiState.Success(Unit)
                }.onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }
}