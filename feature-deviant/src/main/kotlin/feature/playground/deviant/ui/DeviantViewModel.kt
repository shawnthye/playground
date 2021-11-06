package feature.playground.deviant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.entities.Deviation
import core.playground.domain.Result
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.GetPopularDeviantsUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviantViewModel
@Inject constructor(
    getPopularDeviantsUseCase: GetPopularDeviantsUseCase,
) : ViewModel() {
    private val deviantResult: StateFlow<Result<List<Deviation>>> = getPopularDeviantsUseCase(Unit)
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = Result.Loading(null),
        )

    // val deviant = deviantResult.flatMapLatest {
    //     flow {
    //         if (it is Result.Success) {
    //             emit(it.data)
    //         }
    //     }
    // }

    val deviant: StateFlow<Deviation?> = deviantResult.map { it.data?.firstOrNull() }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null,
    )
}
