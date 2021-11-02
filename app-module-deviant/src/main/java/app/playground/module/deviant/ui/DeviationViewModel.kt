package app.playground.module.deviant.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.entities.Deviation
import app.playground.module.deviant.domain.GetDeviantUseCase
import core.playground.domain.Result
import core.playground.domain.data
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviationViewModel @Inject constructor(
    getDeviantUseCase: GetDeviantUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val deviantId: String = savedStateHandle.get<String>("id")!!

    private val deviantResult: Flow<Result<Deviation>> = getDeviantUseCase(deviantId)

    // val deviant = deviantResult.flatMapLatest {
    //     flow {
    //         if (it is Result.Success) {
    //             emit(it.data)
    //         }
    //     }
    // }

    val deviant: StateFlow<Deviation?> = deviantResult.map { it.data }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null,
    )
}
