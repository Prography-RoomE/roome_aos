package com.sevenstars.roome.view.main.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevenstars.domain.usecase.auth.UnlinkUseCase
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnlinkCheckViewModel @Inject constructor(
    private val unlinkUseCase: UnlinkUseCase
): ViewModel() {
    private var _unlinkState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val unlinkState get() = _unlinkState

    fun unlink(reason: String, content: String? = null){
        _unlinkState.value = UiState.Loading

        viewModelScope.launch {
            unlinkUseCase.invoke(
                RoomeApplication.app.userPreferences.getAccessToken().getOrNull().orEmpty(),
                RoomeApplication.app.userPreferences.getLoginProvider().getOrNull().orEmpty(),
                null,
                reason,
                content
            )
                .onSuccess {
                    _unlinkState.value = UiState.Success(Unit)
                }
                .onFailure { code, msg ->
                    _unlinkState.value = UiState.Failure(code, msg)
                }
        }
    }
}