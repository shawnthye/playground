package feature.playground.deviant.art.deviation.ui

import androidx.annotation.LayoutRes
import app.playground.store.database.entities.Deviation
import feature.playground.deviant.R
import java.util.Date

internal sealed class DeviationUiModel(
    @LayoutRes val viewType: Int,
    val action: DeviationDetailAction? = null,
) {
    data class Header(
        val title: String,
    ) : DeviationUiModel(
        R.layout.deviation_detail_list_header,
    )

    data class Author(
        val id: String,
        val name: String,
        val published: Date,
        val avatarUrl: String,
    ) : DeviationUiModel(
        R.layout.deviation_detail_list_author,
        DeviationDetailAction.ViewAuthor(id),
    )

    data class Stats(
        val views: Int,
    ) : DeviationUiModel(
        R.layout.deviation_detail_list_header,
    )
}

internal fun Deviation.toUiModel(): List<DeviationUiModel> {
    val list = mutableListOf<DeviationUiModel>(DeviationUiModel.Header(title = title))

    list += DeviationUiModel.Author(
        id = authorId,
        name = authorName,
        published = published,
        avatarUrl = authorIconUrl,
    )

    list += DeviationUiModel.Author(
        id = authorId,
        name = authorName,
        published = published,
        avatarUrl = authorIconUrl,
    )

    for (i in 1..20) {
        list += DeviationUiModel.Author(
            id = authorId,
            name = authorName,
            published = published,
            avatarUrl = authorIconUrl,
        )
    }

    return list
}
