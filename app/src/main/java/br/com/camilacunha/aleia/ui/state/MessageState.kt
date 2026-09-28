package br.com.camilacunha.aleia.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.camilacunha.aleia.R
import br.com.camilacunha.aleia.ui.screen.MessageScreen

@Composable
fun LoadingState() {
    MessageScreen(
        gifRes = R.raw.vira_pagina,
        title = stringResource(R.string.loading)
    )
}

@Composable
fun EmptyState(
    message: String? = null,
    gifRes: Int,
    actionText: String? = null,
    onAction: () -> Unit
) {
    MessageScreen(
        gifRes = gifRes,
        title = "Carregamento com sucesso!",
        message = message,
        actionText = actionText,
        onAction = onAction
    )
}

@Composable
fun ErrorState(message: String? = null, onRetry: () -> Unit) {
    MessageScreen(
        gifRes = R.raw.estante_vazia,
        title = stringResource(R.string.error_title),
        message = message,
        actionText = stringResource(R.string.try_again),
        onAction = onRetry
    )
}