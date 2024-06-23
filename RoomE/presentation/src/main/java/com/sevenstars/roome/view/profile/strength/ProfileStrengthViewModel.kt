package com.sevenstars.roome.view.profile.strength

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.profile.SaveStrengthsUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileStrengthViewModel @Inject constructor(
    private val saveStrengthsUseCase: SaveStrengthsUseCase
): ViewModel() {

    private var _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState get() = _uiState

    fun setLoadingState(){
        _uiState.value = UiState.Loading
    }

    fun saveData(strengths: List<Int>){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            saveStrengthsUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty(), strengths)
                .onSuccess {
                    _uiState.value = UiState.Success(Unit)
                }.onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }
}