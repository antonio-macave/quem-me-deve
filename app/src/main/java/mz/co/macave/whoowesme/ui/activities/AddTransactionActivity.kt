package mz.co.macave.whoowesme.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.data.DatabaseProvider
import mz.co.macave.whoowesme.data.repository.DebtRepository
import mz.co.macave.whoowesme.data.repository.TransactionRepository
import mz.co.macave.whoowesme.model.Transaction
import mz.co.macave.whoowesme.ui.activities.ui.theme.WhoOwesMeTheme
import mz.co.macave.whoowesme.ui.screen.AddTransactionContent
import mz.co.macave.whoowesme.ui.screen.AppBar
import mz.co.macave.whoowesme.util.toLocalDate
import mz.co.macave.whoowesme.viewmodel.AddTransactionViewModel
import mz.co.macave.whoowesme.viewmodel.ViewModelFactory

class AddTransactionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val debtId = intent.getIntExtra("debtId", -1)
        val transactionId = intent.getIntExtra("transactionId", -1)
        val isEditing = transactionId != -1

        val db = DatabaseProvider.getDatabase(applicationContext)
        val transactionDao = db.transactionDao()
        val debtDao = db.debtDao()
        val transactionRepository = TransactionRepository(transactionDao)
        val debtRepository = DebtRepository(debtDao)
        val factory = ViewModelFactory {
            AddTransactionViewModel(
                transactionRepository = transactionRepository,
                debtRepository = debtRepository
            )
        }

        setContent {

            val viewModel: AddTransactionViewModel by viewModels { factory }

            viewModel.updateDebtId(debtId)
            val debt by viewModel.debt.collectAsState()

            val okEnabled = (viewModel.transactionType.collectAsState().value != 0 || viewModel.transactionType.collectAsState().value != 1) &&
                    viewModel.amount.collectAsState().value.isNotEmpty() &&
                    viewModel.transactionDate.collectAsState().value != null

            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title = if (isEditing)
                                stringResource(R.string.edit_transaction)
                            else
                                stringResource(R.string.title_activity_add_transaction),
                            okEnabled = okEnabled,
                            onCancelListener = { finish() },
                            onOkListener = {
                                val transaction = Transaction(
                                    description = viewModel.description.value,
                                    amount = viewModel.amount.value.toDouble(),
                                    type = viewModel.transactionType.value,
                                    debtId = viewModel.debtId.value!!,
                                    date = viewModel.transactionDate.value!!.toLocalDate()
                                )
                                viewModel.saveTransaction(transaction)
                                finish()
                            }
                        )
                    }
                ) { innerPadding ->

                    LaunchedEffect(debtId) {
                        if (debtId != -1) {
                            viewModel.getDebtData(debtId)
                        }
                    }

                    LaunchedEffect(debt) {
                        debt?.let {
                            viewModel.updateDebtAmount(it.amount)
                            viewModel.updatePaidAmount(it.paidAmount)
                        }
                    }

                    LaunchedEffect(transactionId) {
                        viewModel.updateTransactionId(transactionId)
                    }

                    LaunchedEffect(transactionId) {
                        if (transactionId != -1) {
                            viewModel.loadTransaction(transactionId)
                        }
                    }

                    Column(
                        Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        AddTransactionContent(viewModel)
                    }
                }
            }
        }
    }
}