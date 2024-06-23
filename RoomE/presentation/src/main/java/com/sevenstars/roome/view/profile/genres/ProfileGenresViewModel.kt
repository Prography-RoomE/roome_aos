package com.sevenstars.roome.view.profile.genres

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.profile.SaveGenresUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileGenresViewModel @Inject constructor(
    private val saveGenresUseCase: SaveGenresUseCase
): ViewModel() {

    private var _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState get() = _uiState

    fun setLoadingState(){
        _uiState.value = UiState.Loading
    }

    fun saveData(genres: List<Int>){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            saveGenresUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty(), genres)
                .onSuccess {
                    _uiState.value = UiState.Success(Unit)
                }.onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }
}