package feature.playground.product.hunt.posts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.playground.product.hunt.posts.domain.LoadPostsUseCase
import javax.inject.Inject

@HiltViewModel
internal class DiscoverViewModel @Inject constructor(
    loadPostsUseCase: LoadPostsUseCase,
) : ViewModel() {

    val pagedList = loadPostsUseCase(Unit).cachedIn(viewModelScope)
}
