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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.data.DatabaseProvider
import mz.co.macave.whoowesme.data.repository.DebtorRepository
import mz.co.macave.whoowesme.ui.activities.ui.theme.WhoOwesMeTheme
import mz.co.macave.whoowesme.ui.screen.DebtorsList
import mz.co.macave.whoowesme.viewmodel.DebtorsActivityViewModel
import mz.co.macave.whoowesme.viewmodel.ViewModelFactory

class DebtorsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val scope = rememberCoroutineScope()
            val db = DatabaseProvider.getDatabase(applicationContext)
            val dao = db.debtorDao()
            val repository = DebtorRepository(dao)
            val factory = ViewModelFactory { DebtorsActivityViewModel(repository) }
            val viewModel: DebtorsActivityViewModel by viewModels { factory }
            val snackbarHost = remember { SnackbarHostState() }

            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        DebtorsTopBar(
                            title = stringResource(R.string.debtors),
                            onNavigationClick = { finish() }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        val list by viewModel.debtors.collectAsStateWithLifecycle()
                        DebtorsList(viewModel, list)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtorsTopBar(title: String, onNavigationClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(
                onClick = { onNavigationClick() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}