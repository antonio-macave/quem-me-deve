package mz.co.macave.whoowesme.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import mz.co.macave.whoowesme.R


@Composable
fun ContextMenuButton(
    menuExpanded: Boolean,
    isDeleteConfirmationDialogOpen: Boolean,
    onOpenDeleteConfirmationDialog: () -> Unit,
    onDialogDismissRequest: () -> Unit,
    onContextDismissRequest: () -> Unit,
    onButtonClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column {

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .clickable { onButtonClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }

        ContextMenu(
            menuExpanded = menuExpanded,
            onDismissRequest = { onContextDismissRequest() },
            onOpenDialogClick = { onOpenDeleteConfirmationDialog() },
            onEditClick = { onEditClick() },
        )

        DeleteConfirmationDialog(
            showDialog = isDeleteConfirmationDialogOpen,
            onDialogDismissRequest = { onDialogDismissRequest() },
            onDeleteConfirmation = {  onDeleteClick() }
        )
    }
}


@Composable
fun ContextMenu(
    menuExpanded: Boolean,
    onOpenDialogClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
) {
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDismissRequest() }
    ) {
        DropdownMenuItem(
            modifier = Modifier.padding(vertical = 2.dp),
            onClick = {
                onDismissRequest()
                onEditClick()
            },
            text = { Text(text = stringResource(R.string.edit)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        )

        DropdownMenuItem(
            modifier = Modifier.padding(vertical = 2.dp),
            onClick = {
                onDismissRequest()
                onOpenDialogClick()
            },
            text = { Text(text = stringResource(R.string.delete)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        )
    }
}

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