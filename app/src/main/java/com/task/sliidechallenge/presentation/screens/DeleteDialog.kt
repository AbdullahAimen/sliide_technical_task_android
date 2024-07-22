package com.task.sliidechallenge.presentation.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.task.sliidechallenge.R
import com.task.sliidechallenge.data.models.User

/**
 * @Author Abdullah Abo El~Makarem on 21/07/2024.
 */
@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    user: User?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.delete_user)) },
            text = {
                Text(
                    stringResource(R.string.delete_user_confirmation, user?.name ?: "")
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(stringResource(R.string.delete))
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