package com.sevenstars.roome.view.profile.horror

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.profile.SaveHorrorUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileHorrorViewModel @Inject constructor(
    private val saveHorrorUseCase: SaveHorrorUseCase
): ViewModel() {

    private var _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState get() = _uiState

    fun setLoadingState(){
        _uiState.value = UiState.Loading
    }

    fun saveData(horrorPos: Int){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            saveHorrorUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty(), horrorPos)
                .onSuccess {
                    _uiState.value = UiState.Success(Unit)
                }.onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }
}