package feature.playground.deviant.ui.deviation

internal sealed class DeviationDetailAction {
    data class ViewAuthor(val id: String) : DeviationDetailAction()
}

internal interface DeviationDetailActionListener {
    fun onAction(action: DeviationDetailAction)
}
