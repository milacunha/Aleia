package br.com.camilacunha.aleia.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.ui.screen.MessageScreen

//TODO("trocar o gif de loading")
//TODO("trocar o gif de empty state")
//TODO("trocar o gif de error")

@Composable
fun LoadingState() {
    MessageScreen(
        gifRes = R.raw.loading,
        title = stringResource(R.string.loading)
    )
}

@Composable
fun EmptyState(message: String? = null, actionText: String? = null, onAction: () -> Unit) {
    MessageScreen(
        gifRes = R.raw.empty,
        title = "Carregamento com sucesso!",
        message = message,
        actionText = actionText,
        onAction = onAction
    )
}

@Composable
fun ErrorState(message: String? = null, onRetry: () -> Unit) {
    MessageScreen(
        gifRes = R.raw.error,
        title = stringResource(R.string.error_title),
        message = message,
        actionText = stringResource(R.string.try_again),
        onAction = onRetry
    )
}