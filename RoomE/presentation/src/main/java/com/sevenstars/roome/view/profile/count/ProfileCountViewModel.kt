package com.sevenstars.roome.view.profile.count

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.model.profile.info.CountRange
import com.sevenstars.domain.usecase.profile.SaveRoomCountUseCase
import com.sevenstars.domain.usecase.profile.SaveRoomRangeCountUseCase
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileCountViewModel @Inject constructor(
    private val saveRoomCountUseCase: SaveRoomCountUseCase,
    private val saveRoomRangeCountUseCase: SaveRoomRangeCountUseCase
): ViewModel() {

    private var _uiState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val uiState get() = _uiState

    fun setLoadingState(){
        _uiState.value = UiState.Loading
    }

    fun saveCountData(count: Int, isPlusEnabled: Boolean){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            saveRoomCountUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty(), count, isPlusEnabled)
                .onSuccess {
                    _uiState.value = UiState.Success(Unit)
                }.onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }

    fun saveRangeCountData(range: CountRange){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            saveRoomRangeCountUseCase.invoke(app.userPreferences.getAccessToken().getOrNull().orEmpty(), range.minCount, range.maxCount)
                .onSuccess {
                    _uiState.value = UiState.Success(Unit)
                }.onFailure { code, msg ->
                    _uiState.value = UiState.Failure(code, msg)
                }
        }
    }
}