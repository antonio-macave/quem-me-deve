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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.data.DatabaseProvider
import mz.co.macave.whoowesme.data.repository.TransactionRepository
import mz.co.macave.whoowesme.ui.activities.ui.theme.WhoOwesMeTheme
import mz.co.macave.whoowesme.ui.screen.AddTransactionContent
import mz.co.macave.whoowesme.ui.screen.AppBar
import mz.co.macave.whoowesme.viewmodel.AddTransactionViewModel
import mz.co.macave.whoowesme.viewmodel.ViewModelFactory

class AddTransactionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val db = DatabaseProvider.getDatabase(applicationContext)
            val dao = db.transactionDao()
            val repository = TransactionRepository(dao)
            val factory = ViewModelFactory { AddTransactionViewModel(repository) }
            val viewModel: AddTransactionViewModel by viewModels { factory }

            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title = stringResource(R.string.title_activity_add_transaction),
                            onCancelListener = { finish() },
                            onOkListener = {

                            }
                        )
                    }
                ) { innerPadding ->
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