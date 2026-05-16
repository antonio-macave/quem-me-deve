package mz.co.macave.whoowesme.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.data.DatabaseProvider
import mz.co.macave.whoowesme.data.repository.DebtRepository
import mz.co.macave.whoowesme.model.fabMenuItems
import mz.co.macave.whoowesme.ui.screen.DebtFilter
import mz.co.macave.whoowesme.ui.screen.DebtsList
import mz.co.macave.whoowesme.ui.screen.SortByDialog
import mz.co.macave.whoowesme.ui.theme.WhoOwesMeTheme
import mz.co.macave.whoowesme.viewmodel.MainActivityViewModel
import mz.co.macave.whoowesme.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = DatabaseProvider.getDatabase(applicationContext)
        val debtDao = db.debtDao()
        val repository = DebtRepository(debtDao)
        val factory = ViewModelFactory { MainActivityViewModel(repository) }
        val viewModel: MainActivityViewModel by viewModels { factory }

        setContent {
            WhoOwesMeTheme {

                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val snackBarHost = remember { SnackbarHostState() }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == RESULT_OK) {
                        scope.launch {
                            snackBarHost.showSnackbar(context.getString(R.string.debtor_created))
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackBarHost) },
                    topBar = {
                        TopBar(viewModel = viewModel) {
                            viewModel.updateOverflowMenuExpanded(true)
                        }
                    },
                    floatingActionButton = {
                        FabMenu(viewModel) { index ->
                            if (index == 1) {
                                val intent = Intent(context, CreateDebtActivity::class.java)
                                context.startActivity(intent)
                            } else if (index == 0) {
                                val intent = Intent(context, CreateDebtorActivity::class.java)
                                launcher.launch(intent)
                            }
                        }
                    },
                ) { innerPadding ->

                    val showSortDialog by viewModel.showSortDebtsDialog.collectAsStateWithLifecycle()
                    val debts by viewModel.debts.collectAsStateWithLifecycle(initialValue = emptyList())
                    var filteredList by remember { mutableStateOf(debts) }

                    LaunchedEffect(debts) {
                        filteredList = debts
                    }

                    SortByDialog(showSortDialog, onDismiss = { viewModel.updateShowSortDebtsDialog(false) }) { option ->
                        filteredList = viewModel.sortDebts(debts = filteredList, sortBy = option)
                    }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                    ) {
                        DebtFilter(debts) {
                            filteredList = it
                        }
                        Spacer(Modifier.height(8.dp))
                        DebtsList(viewModel, filteredList) { item ->
                            val intent = Intent(context, TransactionsActivity::class.java).apply {
                                putExtra("debtId", item.debtId)
                                putExtra("debtorId", item.debtorId)
                                putExtra("debtAmount", item.amount)
                                putExtra("paidAmount", item.paidAmount)
                            }
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FabMenu(viewModel: MainActivityViewModel = viewModel(), onClick: (index: Int) -> Unit) {
    val expanded by viewModel.fabMenuExpanded.collectAsStateWithLifecycle()
    FloatingActionButtonMenu(
        expanded = expanded,
        button = {
            ToggleFloatingActionButton(
                checked = expanded,
                onCheckedChange = { viewModel.updateFabMenuExpanded(it) }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(if (expanded) R.drawable.outline_close_24 else R.drawable.outline_add_24),
                    contentDescription = null,
                    tint = if (expanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) {
        fabMenuItems.forEachIndexed { index, item ->
            FloatingActionButtonMenuItem(
                onClick = {
                    viewModel.updateFabMenuExpanded(false)
                    onClick(index)
                },
                text = { Text(text = stringResource(item.name)) },
                icon = { Icon(imageVector = ImageVector.vectorResource(item.iconRes), contentDescription = null) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: MainActivityViewModel, onActionClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = { viewModel.updateShowSortDebtsDialog(true) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = null
                )
            }
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.more_options)
                )
            }

            TopBarOverFlow(viewModel)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )

}

@Composable
fun TopBarOverFlow(viewModel: MainActivityViewModel = viewModel()) {
    val context = LocalContext.current
    val expanded by viewModel.overflowMenuExpanded.collectAsStateWithLifecycle()
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { viewModel.updateOverflowMenuExpanded(false) }
    ) {

        DropdownMenuItem(
            onClick = {
                viewModel.updateOverflowMenuExpanded(false)
                val intent = Intent(context, DebtorsActivity::class.java)
                context.startActivity(intent)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = null
                )
            },
            text = { Text(stringResource(R.string.debtors)) }
        )

    }
}