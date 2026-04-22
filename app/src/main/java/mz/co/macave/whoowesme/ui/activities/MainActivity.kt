package mz.co.macave.whoowesme.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.model.Debt
import mz.co.macave.whoowesme.model.Debtor
import mz.co.macave.whoowesme.model.fabMenuItems
import mz.co.macave.whoowesme.ui.screen.DebtsList
import mz.co.macave.whoowesme.ui.screen.DueDate
import mz.co.macave.whoowesme.ui.theme.WhoOwesMeTheme
import mz.co.macave.whoowesme.viewmodel.MainActivityViewModel
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhoOwesMeTheme {

                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val viewModel: MainActivityViewModel = viewModel()
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
                        TopBar {
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

                    val debt = Debt(
                        id = 1,
                        status = 0,
                        amount = 20000.0,
                        description = "Hello",
                        additionalNotes = "World",
                        debtorId = 1,
                        dueTo = LocalDate.now()
                    )

                    val debtor = Debtor(
                        id = 2,
                        name = "Name",
                        surname = "Surname",
                        contactNumber = "821234567",
                        //debts = listOf(debt)
                    )

                    val list = listOf(debt)
                    DueDate {  }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                    ) {
                        DebtsList(list)
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
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
fun TopBar(onActionClick: () -> Unit) {
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhoOwesMeTheme {
        Greeting("Android")
    }
}