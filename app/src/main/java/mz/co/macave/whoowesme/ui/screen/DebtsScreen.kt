package mz.co.macave.whoowesme.ui.screen

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.model.DebtCardItem
import mz.co.macave.whoowesme.ui.activities.CreateDebtActivity
import mz.co.macave.whoowesme.util.DebtStatus
import mz.co.macave.whoowesme.util.formatLocalDate
import mz.co.macave.whoowesme.util.toMzn
import mz.co.macave.whoowesme.viewmodel.MainActivityViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DebtItem(viewModel: MainActivityViewModel, debt: DebtCardItem, onDebtClick: (DebtCardItem) -> Unit) {

    val context = LocalContext.current

    val progress = (debt.paidAmount / debt.amount).coerceIn(0.0, 1.0).toFloat()
    val paymentOverdue = viewModel.isPaymentOverDue(debt.dueTo)

    var menuExpanded by remember { mutableStateOf(false) }
    var isDeleteConfirmationDialogOpen by remember { mutableStateOf(false) }

    OutlinedCard (
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .clickable(enabled = true, onClick = { onDebtClick(debt) })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NameAndDebtAmount(
                    modifier = Modifier.weight(1f),
                    name = "${debt.debtorName} ${debt.debtorSurname}",
                    amount = debt.amount
                )
                Spacer(Modifier.width(8.dp))
                ContextMenuButton(
                    menuExpanded = menuExpanded,
                    isDeleteConfirmationDialogOpen = isDeleteConfirmationDialogOpen,
                    onDialogDismissRequest  = { isDeleteConfirmationDialogOpen = false },
                    onContextDismissRequest = { menuExpanded = false },
                    onButtonClick = { menuExpanded = true },
                    onOpenDeleteConfirmationDialog = { isDeleteConfirmationDialogOpen = true },
                    onEditClick = {
                        val intent = Intent(context, CreateDebtActivity::class.java).apply {
                            putExtra("debtId", debt.debtId)
                            putExtra("debtorId", debt.debtorId)
                        }
                        context.startActivity(intent)
                    },
                    onDeleteClick = {
                        viewModel.deleteDebtById(debt.debtId)
                    }
                )
            }
            Spacer(Modifier.height(4.dp))
            if (debt.description.isNotEmpty()) {
                Text(
                    text = debt.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
            }

            PaymentProgressBar(
                debtAmount = debt.amount,
                paidAmount = debt.paidAmount
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
            BottomInfo(
                dueTo = debt.dueTo,
                debtAmount = debt.amount,
                paidAmount = debt.paidAmount,
                paymentOverdue = paymentOverdue,
                paymentProgress = progress
            )
        }
    }
}

@Composable
fun DebtStatus(debtStatus: Int) {

    val debtStatusText = when (debtStatus) {
        DebtStatus.PENDING.code -> stringResource(R.string.debt_status_pending)
        DebtStatus.PAID.code -> stringResource(R.string.debt_status_paid)
        DebtStatus.OVERDUE.code -> stringResource(R.string.debt_status_overdue)
        else -> ""
    }

    val debtStatusIcon = when (debtStatus) {
        DebtStatus.PENDING.code -> R.drawable.debt_status_pending
        DebtStatus.PAID.code -> R.drawable.debt_status_paid
        DebtStatus.OVERDUE.code -> R.drawable.debt_status_overdue
        else -> 0
    }

    Row(
        modifier = Modifier
            .background(
                color = if (debtStatus == DebtStatus.OVERDUE.code) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ImageVector.vectorResource(id = debtStatusIcon),
            tint = if (debtStatus == DebtStatus.OVERDUE.code) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = debtStatusText,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun PaymentProgressBar(debtAmount: Double, paidAmount: Double) {
    val progress = (paidAmount / debtAmount).coerceIn(0.0, 1.0).toFloat()
    var currentProgress by remember { mutableFloatStateOf(progress) }
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        LinearProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier.fillMaxWidth(),
            color = ProgressIndicatorDefaults.linearColor,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}

@Composable
fun SortByButton(viewModel: MainActivityViewModel, debts: List<DebtCardItem>, onClick: (List<DebtCardItem>) -> Unit) {
    val sortByOption by viewModel.sortByOption.collectAsStateWithLifecycle()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
            ),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            onClick = { onClick(debts) }
        ) {
            Text(
                text = when(sortByOption) {
                    SortOption.AMOUNT -> stringResource(R.string.sort_by_option_amount)
                    SortOption.DATE -> stringResource(R.string.sort_by_option_date)
                    SortOption.NAME -> stringResource(R.string.sort_by_option_name)
                }
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
}
@Composable
fun DebtFilter(debts: List<DebtCardItem>, onClick: (List<DebtCardItem>) -> Unit) {

    val viewModel: MainActivityViewModel = viewModel()
    val scrollState = rememberScrollState()

    var pending by remember { mutableStateOf(true) }
    var paid by remember { mutableStateOf(true) }
    var overdue by remember { mutableStateOf(true) }

    val all = pending && paid && overdue

    Row(
        modifier = Modifier
            .padding(
                top = 4.dp,
                bottom = 0.dp,
                start = 16.dp,
                end = 16.dp
            )
            .horizontalScroll(state = scrollState)
    ) {
        FilterChip(
            selected = all,
            onClick = {
                val newValue = !all
                pending = newValue
                paid = newValue
                overdue = newValue

                val newFilteredDebts = viewModel.filterDebts(
                    debts = debts,
                    all = all,
                    pending = pending,
                    paid = paid,
                    overdue = overdue
                )
                onClick(newFilteredDebts)
            },
            label = { Text(text = stringResource(R.string.all)) },
            leadingIcon = {
                AnimatedVisibility(all) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        )

        Spacer(Modifier.width(8.dp))

        FilterChip(
            selected = pending,
            onClick = {
                pending = !pending
                val newFilteredDebts = viewModel.filterDebts(
                    debts = debts,
                    all = pending && paid && overdue,
                    pending = pending,
                    paid = paid,
                    overdue = overdue
                )
                onClick(newFilteredDebts)
            },
            label = { Text(text = stringResource(R.string.debt_status_pending)) },
            leadingIcon = {
                AnimatedVisibility(pending) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        )

        Spacer(Modifier.width(8.dp))

        FilterChip(
            selected = paid,
            onClick = {
                paid = !paid
                val newFilteredDebts = viewModel.filterDebts(
                    debts = debts,
                    all = pending && paid && overdue,
                    pending = pending,
                    paid = paid,
                    overdue = overdue
                )
                onClick(newFilteredDebts)
            },
            label = { Text(text = stringResource(R.string.debt_status_paid)) },
            leadingIcon = {
                AnimatedVisibility (paid) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        )

        Spacer(Modifier.width(8.dp))

        FilterChip(
            selected = overdue,
            onClick = {
                overdue = !overdue
                val newFilteredDebts = viewModel.filterDebts(
                    debts = debts,
                    all = pending && paid && overdue,
                    pending = pending,
                    paid = paid,
                    overdue = overdue
                )
                onClick(newFilteredDebts)
            },
            label = { Text(text = stringResource(R.string.debt_status_overdue)) },
            leadingIcon = {
                AnimatedVisibility (overdue) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NameAndDebtAmount(modifier: Modifier = Modifier, name: String, amount: Double) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMediumEmphasized
        )
        Text(
            text = amount.toMzn(),
            style = MaterialTheme.typography.titleMedium
        )

    }
}

@Composable
fun BottomInfoContent(
    modifier: Modifier = Modifier,
    paymentOverdue: Boolean,
    title: String,
    content: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (paymentOverdue && title == stringResource(R.string.due_to)) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.debt_status_overdue),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null
            )
        }

        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun BottomInfo(
    dueTo: LocalDate,
    debtAmount: Double,
    paidAmount: Double,
    paymentProgress: Float,
    paymentOverdue: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.height(8.dp))
        BottomInfoContent(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.due_to),
            paymentOverdue = paymentOverdue,
            content = dueTo.formatLocalDate()
        )
        VerticalDivider(
            modifier = Modifier.fillMaxHeight()
        )
        BottomInfoContent(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.remaining),
            paymentOverdue = paymentOverdue,
            content = (debtAmount - paidAmount).toMzn()
        )
        VerticalDivider()
        BottomInfoContent(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.paid),
            paymentOverdue = paymentOverdue,
            content = "${(paymentProgress * 100).toInt()}%"
        )
    }
}