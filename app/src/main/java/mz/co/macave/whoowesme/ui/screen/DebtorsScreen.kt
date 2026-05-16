package mz.co.macave.whoowesme.ui.screen

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.model.Debt
import mz.co.macave.whoowesme.model.DebtCardItem
import mz.co.macave.whoowesme.model.DebtorWithDebts
import mz.co.macave.whoowesme.ui.activities.TransactionsActivity
import mz.co.macave.whoowesme.util.toMzn
import mz.co.macave.whoowesme.viewmodel.DebtorsActivityViewModel
import mz.co.macave.whoowesme.viewmodel.MainActivityViewModel


@Composable
fun DebtsList(viewModel: MainActivityViewModel, debts: List<DebtCardItem>, onDebtClick: (DebtCardItem) -> Unit) {
    LazyColumn {
        itemsIndexed(items =  debts) { index, item ->
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
        itemsIndexed(items = debtorsWithDebts) { index, item ->
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
    val totalDebt = viewModel.getTotalDebt(debtorsWithDebts)

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { viewModel.updateCardExpanded(debtorsWithDebts.debtor.id) })
   ) {
        Row(
            modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Header(debtorsWithDebts.debtor.name)
            Spacer(Modifier.width(8.dp))
            Column(

            ) {
                Row {
                    Column {
                        Text(
                            text = "${debtorsWithDebts.debtor.name} ${debtorsWithDebts.debtor.surname}",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(Modifier.height(4.dp))
                        TotalDebts(totalDebt)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Spacer(Modifier.height(6.dp))
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
}

@Composable
fun DebtInfo(debt: Debt) {

}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TotalDebts(totalAmount: Double) {
    Column(
    ) {
        Text(
            text = "Total em dívida",
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = totalAmount.toMzn(),
            style = MaterialTheme.typography.labelMedium
        )
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