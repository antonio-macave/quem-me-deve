package mz.co.macave.whoowesme.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.model.Debt
import mz.co.macave.whoowesme.util.DebtStatus
import mz.co.macave.whoowesme.util.toMzn
import mz.co.macave.whoowesme.viewmodel.MainActivityViewModel

@Composable
fun DebtItem(debt: Debt, onClick: (Debt) -> Unit) {
    Card(
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .clickable(enabled = true, onClick = { onClick(debt) })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                DebtStatus(debt.status)
            }
            Text(text = debt.amount.toMzn())
            Text(text = debt.description)
            Text(text = debt.amount.toMzn())
            Text(text = debt.status.toString())
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
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ImageVector.vectorResource(id = debtStatusIcon),
            contentDescription = null
        )
        Spacer(Modifier.width(4.dp))
        Text(text = debtStatusText)
    }
}

@Composable
fun DebtFilter(debts: List<Debt>, onClick: (List<Debt>) -> Unit) {

    val viewModel: MainActivityViewModel = viewModel()

    val scrollState = rememberScrollState()

    var pending by remember { mutableStateOf(true) }
    var paid by remember { mutableStateOf(true) }
    var overdue by remember { mutableStateOf(true) }

    val all = pending && paid && overdue

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
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
