package mz.co.macave.whoowesme.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import mz.co.macave.whoowesme.R


@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onDialogDismissRequest: () -> Unit,
    onDeleteConfirmation: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDialogDismissRequest() },
            title = {
                Text(
                    text = stringResource(R.string.are_you_sure),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDialogDismissRequest()
                        onDeleteConfirmation()
                    }
                ) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDialogDismissRequest() }
                ) {
                    Text(stringResource(android.R.string.cancel))
                }
            },
        )
    }
}