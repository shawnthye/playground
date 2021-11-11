package feature.playground.deviant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.entities.Deviation
import core.playground.domain.Result
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.GetPopularDeviantsUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviantArtViewModel
@Inject constructor(
    getPopularDeviantsUseCase: GetPopularDeviantsUseCase,
) : ViewModel() {

    private val deviantResult: StateFlow<Result<List<Deviation>>> = getPopularDeviantsUseCase(Unit)
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = Result.Loading(null),
        )

    // val deviant2 = deviantResult.flatMapLatest {
    //     flow {
    //         if (it is Result.Success) {
    //             emit(it.data)
    //         }
    //     }
    // }.stateIn()

    val deviant: StateFlow<Deviation?> = deviantResult.mapLatest { it.data?.firstOrNull() }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null,
    )
}
