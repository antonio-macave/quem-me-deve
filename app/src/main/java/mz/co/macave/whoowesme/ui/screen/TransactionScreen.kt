package mz.co.macave.whoowesme.ui.screen

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.model.Transaction
import mz.co.macave.whoowesme.util.TransactionType
import mz.co.macave.whoowesme.util.formatLocalDate
import mz.co.macave.whoowesme.util.toMzn
import mz.co.macave.whoowesme.viewmodel.TransactionsActivityViewModel
import java.time.LocalDate

@Composable
fun TransactionItem(viewModel: TransactionsActivityViewModel, transaction: Transaction) {
    var menuExpanded by remember { mutableStateOf(false) }
    var isConfirmationDialogOpen by remember { mutableStateOf(false) }
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.Today,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = transaction.date.formatLocalDate(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { menuExpanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    TransactionContextMenu(
                        menuExpanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        isConfirmationDialogOpen = true
                    }
                    DeleteSureDialog(
                        isDialogOpen = isConfirmationDialogOpen,
                        viewModel = viewModel,
                        transaction = transaction
                    ) {
                        isConfirmationDialogOpen = false
                    }
                }
            }

            IconAndDescription(
                iconRes = R.drawable.outline_money_24,
                description = when (transaction.type) {
                    TransactionType.CREDIT.type -> "-${transaction.amount.toMzn()}"
                    TransactionType.DEBIT.type -> "+${transaction.amount.toMzn()}"
                    else -> transaction.amount.toMzn()
                }
            )
            if (transaction.description.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                IconAndDescription(R.drawable.description_24, transaction.description)
            }
        }
    }
}


@Composable
fun DeleteSureDialog(
    isDialogOpen: Boolean,
    viewModel: TransactionsActivityViewModel,
    transaction: Transaction,
    onDismissRequest: () -> Unit
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                TextButton( onClick = { viewModel.deleteTransaction(transaction) }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(stringResource(android.R.string.cancel))
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.are_you_sure),
                    style = MaterialTheme.typography.bodySmall
                )
            },
        )
    }
}

@Composable
fun TransactionContextMenu(
    menuExpanded: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteClick: () -> Unit
) {

    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDismissRequest() }
    ) {

        DropdownMenuItem(
            onClick = {
                onDismissRequest()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            },
            text = { Text(text = stringResource(R.string.edit)) }
        )
        DropdownMenuItem(
            onClick = {
                onDeleteClick()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            },
            text = { Text(text = stringResource(R.string.delete)) }
        )
    }
}

@Composable
fun TransactionsList(
    viewModel: TransactionsActivityViewModel,
) {
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())

    if (transactions.isNotEmpty()) {
        LazyColumn {
            itemsIndexed(items = transactions) { _, item ->
                TransactionItem(transaction = item)
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.no_transactions))
        }
    }
}

@Composable
fun IconAndDescription(@DrawableRes iconRes: Int, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(14.dp),
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
