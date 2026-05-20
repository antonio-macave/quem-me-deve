package mz.co.macave.whoowesme.ui.screen

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.model.Transaction
import mz.co.macave.whoowesme.ui.activities.AddTransactionActivity
import mz.co.macave.whoowesme.ui.activities.ui.theme.ErrorContainerDark
import mz.co.macave.whoowesme.ui.activities.ui.theme.ErrorContainerLight
import mz.co.macave.whoowesme.ui.activities.ui.theme.OnErrorContainerDark
import mz.co.macave.whoowesme.ui.activities.ui.theme.OnErrorContainerLight
import mz.co.macave.whoowesme.ui.activities.ui.theme.OnSuccessContainerDark
import mz.co.macave.whoowesme.ui.activities.ui.theme.OnSuccessContainerLight
import mz.co.macave.whoowesme.ui.activities.ui.theme.SuccessContainerDark
import mz.co.macave.whoowesme.ui.activities.ui.theme.SuccessContainerLight
import mz.co.macave.whoowesme.util.TransactionType
import mz.co.macave.whoowesme.util.formatLocalDate
import mz.co.macave.whoowesme.util.toMzn
import mz.co.macave.whoowesme.viewmodel.TransactionsActivityViewModel

@Composable
fun TransactionItem(viewModel: TransactionsActivityViewModel, transaction: Transaction) {
    var menuExpanded by remember { mutableStateOf(false) }
    var isConfirmationDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable{ }
    ) {
        TransactionHeader(
            transactionType = transaction.type,
            date = transaction.date.formatLocalDate()
        ) {
            Spacer(Modifier.weight(1f))
            ContextMenuButton(
                menuExpanded = menuExpanded,
                isDeleteConfirmationDialogOpen = isConfirmationDialogOpen,
                onOpenDeleteConfirmationDialog = { isConfirmationDialogOpen = true },
                onDialogDismissRequest = { isConfirmationDialogOpen = false },
                onContextDismissRequest = { menuExpanded = false },
                onButtonClick = { menuExpanded = true },
                onEditClick = {
                    val intent = Intent(context, AddTransactionActivity::class.java).apply {
                        putExtra("transactionId", transaction.id)
                        putExtra("debtId", transaction.debtId)
                    }
                    context.startActivity(intent)
                },
                onDeleteClick = {
                    viewModel.deleteTransaction(transaction)
                }
            )
        }
        TransactionAmount(
            amount = transaction.amount,
            transactionType = transaction.type
        )
        if (transaction.description.isNotEmpty()) {
            TransactionDescription(transaction.description)
        }
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
                TransactionItem(transaction = item, viewModel = viewModel)
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

@Composable
fun TransactionHeader(
    transactionType: Int,
    date: String,
    contextMenuButton: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (transactionType == TransactionType.CREDIT.type)
                            if(isSystemInDarkTheme())
                                SuccessContainerDark
                            else
                                SuccessContainerLight
                        else
                            if(isSystemInDarkTheme())
                                ErrorContainerDark
                            else
                                ErrorContainerLight,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = if (transactionType == TransactionType.CREDIT.type)
                        Icons.Default.ArrowDownward
                    else
                        Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = if (transactionType == TransactionType.CREDIT.type)
                        if(isSystemInDarkTheme())
                            OnSuccessContainerDark
                        else
                            OnSuccessContainerLight
                    else
                        if(isSystemInDarkTheme())
                            OnErrorContainerDark
                        else
                            OnErrorContainerLight
                )
            }
            Spacer(Modifier.width(16.dp))
            Column() {
                Text(
                    text = when (transactionType) {
                        TransactionType.CREDIT.type -> stringResource(R.string.payment)
                        TransactionType.DEBIT.type -> stringResource(R.string.addition)
                        else -> ""
                    },
                    color = if (transactionType == TransactionType.CREDIT.type)
                        if(isSystemInDarkTheme())
                            OnSuccessContainerDark
                        else
                            OnSuccessContainerLight
                    else
                        if(isSystemInDarkTheme())
                            OnErrorContainerDark
                        else
                            OnErrorContainerLight,
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            contextMenuButton()
        }
    }
}

@Composable
fun TransactionAmount(transactionType: Int, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (transactionType == TransactionType.DEBIT.type) {
                "+"
            } else {
                "-"
            },
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = amount.toMzn(),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun TransactionDescription(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 0.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}