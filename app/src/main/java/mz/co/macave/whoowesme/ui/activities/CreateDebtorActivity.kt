package mz.co.macave.whoowesme.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import mz.co.macave.whoowesme.ui.activities.ui.theme.WhoOwesMeTheme
import mz.co.macave.whoowesme.ui.screen.CreateDebtorContent

class CreateDebtorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhoOwesMeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CreateDebtorTopBar(
                            title = stringResource(R.string.title_activity_create_debtor),
                            onCancelListener = { finish() },
                            onOkListener = {  }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDebtorTopBar(title: String, onCancelListener: () -> Unit, onOkListener: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = { onCancelListener() }
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }
        },
        actions = {
            IconButton(
                onClick = { onOkListener() }
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null
                )
            }
        }
    )
}