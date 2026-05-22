package mz.co.macave.quemmedeve.ui.screen

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.quemmedeve.R
import mz.co.macave.quemmedeve.model.DebtCardItem
import mz.co.macave.quemmedeve.model.DebtorWithDebts
import mz.co.macave.quemmedeve.ui.activities.CreateDebtorActivity
import mz.co.macave.quemmedeve.util.toMzn
import mz.co.macave.quemmedeve.viewmodel.DebtorsActivityViewModel
import mz.co.macave.quemmedeve.viewmodel.MainActivityViewModel


@Composable
fun DebtsList(
    viewModel: MainActivityViewModel,
    debts: List<DebtCardItem>,
    onDebtClick: (DebtCardItem) -> Unit
) {
    LazyColumn {
        if (debts.isNotEmpty()) {
            item {
                SortByButton(
                    viewModel = viewModel,
                    debts = debts,
                    onClick = {
                        viewModel.updateShowSortDebtsDialog(true)
                    }
                )
            }
        }
        itemsIndexed(items = debts) { index, item ->
            DebtItem(viewModel, debt = debts[index]) { onDebtClick(item) }
        }
    }
}


@Composable
fun Header(debtorName: String) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = debtorName.first().toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
fun DebtorsList(
    viewModel: DebtorsActivityViewModel = viewModel(),
    debtorsWithDebts: List<DebtorWithDebts>
) {
    LazyColumn {
        items(items = debtorsWithDebts) { item ->
            DebtorItem(
                viewModel = viewModel,
                debtorsWithDebts = item
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DebtorItem(
    viewModel: DebtorsActivityViewModel = viewModel(),
    debtorsWithDebts: DebtorWithDebts
) {

    val context = LocalContext.current
    val visible by viewModel.cardExpanded.collectAsStateWithLifecycle()
    var menuExpanded by remember { mutableStateOf(false) }
    var isConfirmationDialogOpen by remember { mutableStateOf(false) }
    val totalDebt = viewModel.getTotalDebt(debtorsWithDebts)
    val debtsCount = debtorsWithDebts.debts.size

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { viewModel.updateCardExpanded(debtorsWithDebts.debtor.id) })
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row {
                Header(debtorsWithDebts.debtor.name)
                Spacer(Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(0.9f)
                ) {
                    Row {
                        Column {
                            Text(
                                text = "${debtorsWithDebts.debtor.name} ${debtorsWithDebts.debtor.surname}",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Spacer(Modifier.height(8.dp))
                            TotalDebts(
                                totalAmount = totalDebt,
                                debtsCount = debtsCount
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                }
                ContextMenuButton(
                    menuExpanded = menuExpanded,
                    isDeleteConfirmationDialogOpen = isConfirmationDialogOpen,
                    onOpenDeleteConfirmationDialog = { isConfirmationDialogOpen = true },
                    onDialogDismissRequest = { isConfirmationDialogOpen = false },
                    onContextDismissRequest = { menuExpanded = false },
                    onButtonClick = { menuExpanded = true },
                    onEditClick = {
                        val intent = Intent(context, CreateDebtorActivity::class.java).apply {
                            putExtra("debtorId", debtorsWithDebts.debtor.id)
                        }
                        context.startActivity(intent)
                    },
                    onDeleteClick = {
                        viewModel.deleteDebtor(debtorsWithDebts.debtor)
                    }
                )
            }
            Spacer(Modifier.height(8.dp))
            AnimatedVisibility(
                visible = visible == debtorsWithDebts.debtor.id,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                BottomButtons(
                    onPrimaryClick = {

                    },
                    onSecondaryClick = {
                        val intent = Intent(context, TransactionsActivity::class.java).apply {
                            putExtra("debtorId", debtorsWithDebts.debtor.id)
                            putExtra("debtorName", "${debtorsWithDebts.debtor.name} ${debtorsWithDebts.debtor.surname}")
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TotalDebts(totalAmount: Double, debtsCount: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.total_debt),
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = totalAmount.toMzn(),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(4.dp)
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = pluralStringResource(R.plurals.debts_count, debtsCount, debtsCount),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }
}

@Composable
fun BottomButtons(onPrimaryClick: () -> Unit, onSecondaryClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        OutlinedButton(
            onClick = onSecondaryClick
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_balance_24),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(text = stringResource(R.string.see_debts))
        }
        Spacer(Modifier.width(8.dp))
        Button(
            onClick = onPrimaryClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(text = stringResource(R.string.debt))
        }
    }
}