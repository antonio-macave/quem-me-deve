package mz.co.macave.whoowesme.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import mz.co.macave.whoowesme.ui.activities.ui.theme.WhoOwesMeTheme

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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                }
            }
        }
    }
}