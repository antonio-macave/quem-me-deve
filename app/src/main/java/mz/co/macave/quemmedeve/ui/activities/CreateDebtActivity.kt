package mz.co.macave.quemmedeve.ui.activities

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mz.co.macave.quemmedeve.R
import mz.co.macave.quemmedeve.data.DatabaseProvider
import mz.co.macave.quemmedeve.data.repository.DebtRepository
import mz.co.macave.quemmedeve.data.repository.DebtorRepository
import mz.co.macave.quemmedeve.ui.screen.AppBar
import mz.co.macave.quemmedeve.ui.screen.CreateDebt
import mz.co.macave.quemmedeve.ui.theme.WhoOwesMeTheme
import mz.co.macave.quemmedeve.viewmodel.CreateDebtViewModel
import mz.co.macave.quemmedeve.viewmodel.ViewModelFactory

class CreateDebtActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val debtId = intent.getIntExtra("debtId", -1)
            val debtorId = intent.getIntExtra("debtorId", -1)
            val isEditing = debtId != -1 && debtorId != -1

            val db = DatabaseProvider.getDatabase(applicationContext)
            val debtDao = db.debtDao()
            val debtorDao = db.debtorDao()
            val debtRepository = DebtRepository(debtDao)
            val debtorRepository = DebtorRepository(debtorDao)
            val factory = ViewModelFactory {
                CreateDebtViewModel(debtRepository, debtorRepository)
            }

            val viewModel: CreateDebtViewModel by viewModels { factory }
            val saveButtonEnabled = viewModel.selectedDebtor.collectAsState().value != null
                    && viewModel.amount.collectAsState().value.isNotEmpty()
                    && viewModel.dueToDate.collectAsState().value != null
                    && !viewModel.isAmountError.collectAsState().value



            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title = if (isEditing)
                                    stringResource(R.string.edit_debt)
                                else
                                    stringResource(R.string.title_activity_create_debt),
                            onCancelListener = { finish() },
                            okEnabled = saveButtonEnabled,
                            okButtonText = if (isEditing)
                                stringResource(R.string.update)
                            else
                                stringResource(R.string.save),
                            onOkListener = {
                                viewModel.save()
                                finish()
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        LaunchedEffect(debtId) {
                            if (debtId != -1) {
                                viewModel.updateDebtId(debtId)
                            }
                        }

                        LaunchedEffect(debtId) {
                            if (debtId != -1) {
                                viewModel.loadDebt(debtId, debtorId)
                            }
                        }

                        CreateDebt(viewModel)
                    }
                }
            }
        }
    }
}