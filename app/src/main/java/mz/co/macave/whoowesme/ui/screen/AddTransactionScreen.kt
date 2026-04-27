package mz.co.macave.whoowesme.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.util.TransactionType
import mz.co.macave.whoowesme.viewmodel.AddTransactionViewModel


@Composable
fun AddTransactionContent(viewModel: AddTransactionViewModel) {
    val transactionType by viewModel.transactionType.collectAsStateWithLifecycle()
    val isTotalPayment by viewModel.isTotalPayment.collectAsStateWithLifecycle()
    TransactionTypeSelector(viewModel, transactionType) {
        viewModel.updateTransactionType(it)
    }
    Spacer(Modifier.height(16.dp))
    AnimatedVisibility(
        visible = transactionType == 0,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        TotalPaymentSwitch(isTotalPayment) {
            viewModel.updateIsTotalPayment(!isTotalPayment)
        }
    }
    Spacer(Modifier.height(16.dp))
    TransactionAmount(isTotalPayment)
    Spacer(Modifier.height(16.dp))
    SelectTransactionDate()
    Spacer(Modifier.height(16.dp))
    TransactionDescription()
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TransactionTypeSelector(viewModel: AddTransactionViewModel, selectedOption: Int, onSelectedIndex: (Int) -> Unit) {
    val options = TransactionType.entries
    Column() {
        Text(text = stringResource(R.string.transaction_type))
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
        ) {
            val modifiers = listOf(Modifier.weight(1f), Modifier.weight(1f))
            options.fastForEachIndexed { index, item ->
                ToggleButton(
                    modifier = modifiers[index].semantics { role = Role.RadioButton },
                    checked = index == selectedOption,
                    onCheckedChange = { viewModel.updateTransactionType(index) },
                    shapes = when (index) {
                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        else -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    }
                ) {
                    AnimatedVisibility(
                        visible = index == selectedOption
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.width(ToggleButtonDefaults.IconSpacing))
                    Text(
                        text = when (item) {
                            TransactionType.DEBIT -> stringResource(R.string.payment)
                            TransactionType.CREDIT -> stringResource(R.string.addition)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionAmount(isFullPayment: Boolean) {
    var amount by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = amount,
        readOnly = isFullPayment, //If full payment, read-only
        onValueChange = { amount = it },
        label = { Text(text = stringResource(R.string.amount)) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_money_24),
                contentDescription = null
            )
        },
        suffix = { Text( text = "MZN") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}

@Composable
fun TransactionDescription(viewModel: AddTransactionViewModel = viewModel()) {
    val description by viewModel.description.collectAsStateWithLifecycle()
    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = description,
        onValueChange = { viewModel.updateDescription(it) },
        label = { Text(text = stringResource(R.string.description)) },
        maxLines = 3,
        minLines = 3,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
fun TotalPaymentSwitch(isTotalPayment: Boolean, onSwitchChecked: () -> Unit) {
    Row(
        modifier = Modifier
            .selectable(
                selected = isTotalPayment,
                onClick = { onSwitchChecked() },
                role = Role.RadioButton
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.full_payment),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.weight(1f))
        Switch(
            checked = isTotalPayment,
            onCheckedChange = { onSwitchChecked() }
        )
    }
}

@Preview
@Composable
fun TransactionTypePreview() {
    TransactionTypeSelector()
}