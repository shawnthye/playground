package feature.playground.deviant.ui.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.entities.DeviationEntity
import core.playground.domain.Result
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.LoadPopularDeviantsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviantTrackViewModel
@Inject constructor(
    loadPopularDeviantsUseCase: LoadPopularDeviantsUseCase,
) : ViewModel(), DeviantTrackAdapter.OnClickListener {

    private val _refreshingAction = Channel<Unit>(Channel.CONFLATED).apply {
        trySend(Unit) // init loading
    }

    private val resultState: Flow<Result<List<DeviationEntity>>> = _refreshingAction
        .receiveAsFlow()
        .flatMapLatest { loadPopularDeviantsUseCase(Unit) }.stateIn(
            viewModelScope,
            WhileViewSubscribed,
            Result.Loading(),
        )

    val deviationsState: StateFlow<List<DeviationEntity>> = resultState.map {
        it.data ?: emptyList()
    }.stateIn(
        viewModelScope,
        WhileViewSubscribed,
        emptyList(),
    )

    val isRefreshing = resultState.map {
        it is Result.Loading
    }.stateIn(
        viewModelScope,
        WhileViewSubscribed,
        true,
    )

    private val _idAction = Channel<String>(capacity = Channel.CONFLATED)
    val idActions = _idAction.receiveAsFlow()

    fun onSwipeRefresh() {
        _refreshingAction.trySend(Unit)
    }

    override fun onClicked(id: String) {
        _idAction.trySend(id)
    }
}
