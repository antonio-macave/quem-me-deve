package mz.co.macave.whoowesme.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.util.SortOption

@Composable
fun SortByDialog(isOpen: Boolean, onDismiss: () -> Unit, onConfirmation: (Int) -> Unit) {
    if (isOpen) {
        Dialog(onDismissRequest = onDismiss) {
            SortByContent(onDismiss = onDismiss, onConfirmation = onConfirmation)
        }
    }
}

@Composable
fun SortByContent(onDismiss: () -> Unit, onConfirmation: (Int) -> Unit) {
    val options = SortOption.entries
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(SortOption.NAME) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = stringResource(R.string.sort_by_title),
            color = AlertDialogDefaults.textContentColor,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Column(modifier = Modifier.selectableGroup()) {
            options.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (selectedOption == item),
                            onClick = { onOptionSelected(item) },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedOption == item),
                        onClick = { }
                    )
                    Text(text = when (item) {
                        SortOption.NAME -> stringResource(R.string.sort_by_option_name)
                        SortOption.DATE -> stringResource(R.string.sort_by_option_date)
                        SortOption.AMOUNT -> stringResource(R.string.sort_by_option_amount)
                    })
                }
            }
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal =16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { onDismiss() }
                ) { Text(text = stringResource(android.R.string.cancel)) }
                Spacer(Modifier.width(8.dp))
                TextButton(
                    onClick = {
                        onConfirmation(selectedOption.option)
                        onDismiss()
                    }
                ) { Text(text = stringResource(android.R.string.ok)) }
            }
        }
    }
}