package mz.co.macave.whoowesme.ui.screen

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.model.Debt
import mz.co.macave.whoowesme.model.Debtor
import mz.co.macave.whoowesme.ui.activities.TransactionsActivity
import mz.co.macave.whoowesme.viewmodel.MainActivityViewModel
import java.time.LocalDate


@Composable
fun DebtsList(debts: List<Debt>) {
    val context = LocalContext.current
    LazyColumn {
        itemsIndexed(items =  debts) { index, item ->
            DebtItem(debt = debts[index]) {
                val intent = Intent(context, TransactionsActivity::class.java).apply {
                    putExtra("debtId", item.id)
                }
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun DebtorSituation(balance: Double) {
    val debtorSituation = if (balance >= 0) stringResource(R.string.debt_status_pending) else stringResource(R.string.debt_status_overdue)
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 4.dp
            )
    ){
        Text(
            text = debtorSituation,
            fontSize = 14.sp
        )
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
    debtors: List<Debtor>
) {
    LazyColumn {
        itemsIndexed(items = debtors) { index, item ->
            DebtorItem(
                viewModel = viewModel,
                debtor = item
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DebtorItem(
    viewModel: MainActivityViewModel = viewModel(),
    debtor: Debtor
) {

    val context = LocalContext.current
    val visible by viewModel.cardExpanded.collectAsStateWithLifecycle()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { viewModel.updateCardExpanded(debtor.id) }),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
   ) {
        Row(
            modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Header(debtor.name)
            Spacer(Modifier.width(8.dp))
            Column(

            ) {
                Row {
                    Column {
                        Text(
                            text = "${debtor.name} ${debtor.surname}",
                            style = MaterialTheme.typography.displaySmallEmphasized,
                            fontSize = 20.sp
                        )

                        Text(
                            text = /* "${debt.status}  MZN"*/  "20.000 MZN",
                            style = MaterialTheme.typography.bodyMedium,
                            //color = if (debt.amount >= 0) GoodSituation else RedSituation
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                DebtorSituation(200.00)
                Spacer(Modifier.height(6.dp))
                AnimatedVisibility(
                    visible = visible == debtor.id,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    BottomButtons(
                        onPrimaryClick = {

                        },
                        onSecondaryClick = {
                            val intent = Intent(context, TransactionsActivity::class.java).apply {
                                putExtra("debtorId", debtor.id)
                                putExtra("debtorName", "${debtor.name} ${debtor.surname}")
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun Previews() {
    //BottomButtons(onPrimaryClick = { }) { }
    val now = LocalDate.now()
    val debt = Debt(
        id = 1,
        status = 0,
        description = "Car",
        additionalNotes = "5 prestações",
        amount = 20000.0,
        debtorId = 1,
        dueTo = now
    )


    val debtor = Debtor(
        id = 2,
        name = "Name",
        surname = "Surname",
        contactNumber = "821234567",

    )

    DebtorItem(debtor = debtor)
    //DebtorSituation(0.0)
    //Header("Tonny")
}