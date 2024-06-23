package com.sevenstars.roome.view.profile.dislike

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.profile.SaveDislikeUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDislikeViewModel @Inject constructor(
    private val saveDislikeUseCase: SaveDislikeUseCase
): ViewModel() {

    private var _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState get() = _uiState

    fun setLoadingState(){
        _uiState.value = UiState.Loading
    }

    fun saveData(dislikeFactors: List<Int>){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            saveDislikeUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty(), dislikeFactors)
                .onSuccess {
                    _uiState.value = UiState.Success(Unit)
                }.onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }
}