package mz.co.macave.quemmedeve.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.quemmedeve.R
import mz.co.macave.quemmedeve.data.DatabaseProvider
import mz.co.macave.quemmedeve.data.repository.DebtRepository
import mz.co.macave.quemmedeve.data.repository.DebtorRepository
import mz.co.macave.quemmedeve.data.repository.TransactionRepository
import mz.co.macave.quemmedeve.ui.screen.TransactionsList
import mz.co.macave.quemmedeve.ui.theme.WhoOwesMeTheme
import mz.co.macave.quemmedeve.viewmodel.TransactionsActivityViewModel
import mz.co.macave.quemmedeve.viewmodel.ViewModelFactory

class TransactionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val debtId = intent.getIntExtra("debtId",0)
            val debtorId = intent.getIntExtra("debtorId", 0)
            val debtAmount = intent.getDoubleExtra("debtAmount", 0.0)
            val paidAmount = intent.getDoubleExtra("paidAmount", 0.0)

            val db = DatabaseProvider.getDatabase(applicationContext)
            val transactionDao = db.transactionDao()
            val debtorDao = db.debtorDao()
            val debtDao = db.debtDao()
            val transactionRepository = TransactionRepository(transactionDao)
            val debtorRepository = DebtorRepository(debtorDao)
            val debtRepository = DebtRepository(debtDao)
            val factory = ViewModelFactory {
                TransactionsActivityViewModel(
                    transactionRepository,
                    debtRepository,
                    debtorRepository
                )
            }
            val viewModel: TransactionsActivityViewModel = viewModel(factory = factory)

            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TransactionsTopAppBar(stringResource(R.string.title_activity_transactions), viewModel) { finish() } }
                ) { innerPadding ->

                    LaunchedEffect(debtId, debtorId)  {
                        viewModel.updateDebtId(debtId)
                        viewModel.updateDebtorId(debtorId)
                        viewModel.updateDebtAmount(debtAmount)
                        viewModel.updatePaidAmount(paidAmount)
                    }

                    Column(Modifier.padding(innerPadding)) {
                        TransactionsList(viewModel)
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TransactionsTopAppBar(title: String?, viewModel: TransactionsActivityViewModel,onNavigationButtonClick: () -> Unit) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            if (title != null) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationButtonClick) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(
                onClick = {
                    val intent = Intent(context, AddTransactionActivity::class.java).apply {
                        putExtra("debtId", viewModel.debtId.value)
                        putExtra("debtAmount", viewModel.debtAmount.value)
                        putExtra("paidAmount", viewModel.paidAmount.value)
                    }
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.title_activity_add_transaction),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}