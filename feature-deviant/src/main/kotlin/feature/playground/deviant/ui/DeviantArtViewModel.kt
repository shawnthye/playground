package feature.playground.deviant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.playground.entities.DeviationEntity
import core.playground.domain.Result
import core.playground.ui.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.deviant.domain.LoadPopularDeviantsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviantArtViewModel
@Inject constructor(
    loadPopularDeviantsUseCase: LoadPopularDeviantsUseCase,
) : ViewModel(), DeviantArtsAdapter.OnClickListener {

    val resultState: StateFlow<Result<List<DeviationEntity>>> =
        loadPopularDeviantsUseCase(Unit).stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = Result.Loading(emptyList()),
        )

    private val _idAction = Channel<String>(capacity = Channel.CONFLATED)
    val idActions = _idAction.receiveAsFlow()

    override fun onClicked(id: String) {
        _idAction.trySend(id)
    }
}
