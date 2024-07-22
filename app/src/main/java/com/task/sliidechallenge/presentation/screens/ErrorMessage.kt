package com.task.sliidechallenge.presentation.screens

/**
 * @Author Abdullah Abo El~Makarem on 21/07/2024.
 */

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.task.sliidechallenge.R

@Composable
fun ErrorMessage(
    showDialog: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.delete_user)) },
            text = {
                Text(message)
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Preview
@Composable
fun ErrorMessagePreview() {
    ErrorMessage(true,
        message = "An error occurred while loading data.",
        onDismiss = {}
    )
}