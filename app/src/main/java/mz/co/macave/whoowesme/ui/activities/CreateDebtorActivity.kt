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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.runtime.collectAsState

class CreateDebtorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val db = DatabaseProvider.getDatabase(applicationContext)
        val dao = db.debtorDao()
        val repository = DebtorRepository(dao)
        val factory = ViewModelFactory { CreateDebtorViewModel(repository) }
        val viewModel: CreateDebtorViewModel by viewModels { factory }

        val debtorId = intent.getIntExtra("debtorId", -1)
        val isEditing = debtorId != -1

        setContent {

            val scope = rememberCoroutineScope()
            val snackbarHost = remember { SnackbarHostState() }

            val okEnabled =
                viewModel.name.collectAsState().value.isNotEmpty() &&
                viewModel.surname.collectAsState().value.isNotEmpty()

            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title = stringResource(R.string.title_activity_create_debtor),
                            onCancelListener = { finish() },
                            okEnabled = okEnabled,
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
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHost) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        LaunchedEffect(debtorId) {
                            viewModel.updateDebtorId(debtorId)
                        }

                        LaunchedEffect(debtorId) {
                            if (debtorId != -1) {
                                viewModel.loadDebtor(debtorId)
                            }
                        }

                        CreateDebtorContent(viewModel)
                    }
                }
            }
        }
    }
}