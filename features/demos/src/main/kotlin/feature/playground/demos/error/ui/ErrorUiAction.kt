package feature.playground.demos.error.ui

internal sealed class ErrorUiAction {
    object Okhttp : ErrorUiAction()
    object Response204 : ErrorUiAction()
}
