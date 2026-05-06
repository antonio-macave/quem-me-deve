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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.data.DatabaseProvider
import mz.co.macave.whoowesme.data.repository.DebtRepository
import mz.co.macave.whoowesme.data.repository.DebtorRepository
import mz.co.macave.whoowesme.model.Debt
import mz.co.macave.whoowesme.ui.screen.AppBar
import mz.co.macave.whoowesme.ui.screen.CreateDebt
import mz.co.macave.whoowesme.ui.theme.WhoOwesMeTheme
import mz.co.macave.whoowesme.util.DebtStatus
import mz.co.macave.whoowesme.util.toLocalDate
import mz.co.macave.whoowesme.viewmodel.CreateDebtViewModel
import mz.co.macave.whoowesme.viewmodel.ViewModelFactory

class CreateDebtActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val db = DatabaseProvider.getDatabase(applicationContext)
            val debtDao = db.debtDao()
            val debtorDao = db.debtorDao()
            val debtRepository = DebtRepository(debtDao)
            val debtorRepository = DebtorRepository(debtorDao)
            val factory = ViewModelFactory {
                CreateDebtViewModel(debtRepository, debtorRepository)
            }
            val viewModel: CreateDebtViewModel by viewModels { factory }

            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title = stringResource(R.string.title_activity_create_debt),
                            onCancelListener = { finish() },
                            onOkListener = {  }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        CreateDebt(viewModel)
                    }
                }
            }
        }
    }
}