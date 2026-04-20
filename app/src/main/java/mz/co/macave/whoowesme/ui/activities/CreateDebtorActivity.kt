package mz.co.macave.whoowesme.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.data.DatabaseProvider
import mz.co.macave.whoowesme.data.repository.DebtorRepository
import mz.co.macave.whoowesme.ui.activities.ui.theme.WhoOwesMeTheme
import mz.co.macave.whoowesme.ui.screen.AppBar
import mz.co.macave.whoowesme.ui.screen.CreateDebtorContent
import mz.co.macave.whoowesme.viewmodel.CreateDebtorViewModel
import mz.co.macave.whoowesme.viewmodel.ViewModelFactory

class CreateDebtorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val db = DatabaseProvider.getDatabase(applicationContext)
            val dao = db.debtorDao()
            val repository = DebtorRepository(dao)
            val factory = ViewModelFactory { CreateDebtorViewModel(repository) }
            val viewModel: CreateDebtorViewModel by viewModels { factory }

            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title = stringResource(R.string.title_activity_create_debtor),
                            onCancelListener = { finish() },
                            onOkListener = {
                                viewModel.saveDebtor(
                                    name = viewModel.name.value,
                                    surname = viewModel.surname.value,
                                    contactNumber = viewModel.contactNumber.value
                                )
                                setResult(RESULT_OK)
                                finish()
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        CreateDebtorContent(viewModel)
                    }
                }
            }
        }
    }
}