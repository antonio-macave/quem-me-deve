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
import mz.co.macave.whoowesme.ui.activities.ui.theme.WhoOwesMeTheme
import mz.co.macave.whoowesme.ui.screen.AddTransactionContent
import mz.co.macave.whoowesme.ui.screen.AppBar
import mz.co.macave.whoowesme.util.TransactionType
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

            val saveButtonEnabled = (viewModel.transactionType.collectAsState().value == TransactionType.CREDIT.type
                    || viewModel.transactionType.collectAsState().value == TransactionType.DEBIT.type)
                    && viewModel.amount.collectAsState().value.isNotEmpty()
                    && viewModel.transactionDate.collectAsState().value != null
                    && !viewModel.debtAmountError.collectAsState().value
                    && viewModel.remainingBalance.collectAsState().value >= 0

            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title = if (isEditing)
                                stringResource(R.string.edit_transaction)
                            else
                                stringResource(R.string.title_activity_add_transaction),
                            okEnabled = saveButtonEnabled,
                            okButtonText = if (isEditing)
                                    stringResource(R.string.update)
                                else
                                    stringResource(R.string.save),
                            onCancelListener = { finish() },
                            onOkListener = {
                                viewModel.save()
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
                        AddTransactionContent(isEditing = isEditing, viewModel = viewModel)
                    }
                }
            }
        }
    }
}