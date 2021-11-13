package feature.playground.deviant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.entities.DeviationEntities
import core.playground.domain.Result
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.GetPopularDeviantsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviantArtViewModel
@Inject constructor(
    getPopularDeviantsUseCase: GetPopularDeviantsUseCase,
) : ViewModel(), DeviantArtsAdapter.OnClickListener {

    private val deviationsState: StateFlow<Result<List<DeviationEntities>>> =
        getPopularDeviantsUseCase(Unit).stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = Result.Loading(null),
        )

    val isLoading = deviationsState.mapLatest { it is Result.Loading }
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = true,
        )

    val deviations = deviationsState.flatMapLatest {
        flow {
            if (it is Result.Success) {
                emit(it.data)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = emptyList(),
    )

    private val _idAction = Channel<String>(capacity = Channel.CONFLATED)
    val idActions = _idAction.receiveAsFlow()

    override fun onClicked(id: String) {
        _idAction.trySend(id)
    }
}
