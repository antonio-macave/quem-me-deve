package mz.co.macave.whoowesme.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import mz.co.macave.whoowesme.util.formatDateFromMillis
import mz.co.macave.whoowesme.util.toMzn
import mz.co.macave.whoowesme.viewmodel.AddTransactionViewModel


@Composable
fun AddTransactionContent(viewModel: AddTransactionViewModel) {
    val transactionType by viewModel.transactionType.collectAsStateWithLifecycle()
    val isTotalPayment by viewModel.isTotalPayment.collectAsStateWithLifecycle()

    CurrentDebtBalance(viewModel = viewModel)
    Spacer(Modifier.height(8.dp))
    TransactionTypeSelector(selectedOption = transactionType) { index ->
        viewModel.updateTransactionType(index)
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
    TransactionAmount(viewModel = viewModel, isFullPayment = isTotalPayment)
    Spacer(Modifier.height(16.dp))
    SelectTransactionDate()
    Spacer(Modifier.height(16.dp))
    TransactionDescription()
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TransactionTypeSelector(selectedOption: Int, onSelectedIndex: (Int) -> Unit) {
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
                    onCheckedChange = { onSelectedIndex(index) },
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
fun TransactionAmount(viewModel: AddTransactionViewModel = viewModel(), isFullPayment: Boolean) {
    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val remainingBalance by viewModel.remainingBalance.collectAsStateWithLifecycle()
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = amount,
        readOnly = isFullPayment, //If full payment, read-only
        onValueChange = {
            viewModel.updateAmount(it)
            if (it.isNotEmpty()) {
                viewModel.calculateRemainingBalance(it.toDouble())
            }
        },
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CurrentDebtBalance(viewModel: AddTransactionViewModel) {
    val balance by viewModel.debtBalance.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.debt_balance),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = balance.toMzn(),
            style = MaterialTheme.typography.headlineMediumEmphasized,
            color = MaterialTheme.colorScheme.primary
        )
    }
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

@Composable
fun SelectTransactionDate(viewModel: AddTransactionViewModel = viewModel()) {

    val date by viewModel.transactionDate.collectAsStateWithLifecycle()
    val showDialog by viewModel.showDatePicker.collectAsStateWithLifecycle()
    val datePickerState = rememberDatePickerState()

    if (showDialog) {

        DatePickerDialog(
            onDismissRequest = { viewModel.updateShowDatePicker(false) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateSelectedDate(datePickerState.selectedDateMillis!!)
                        viewModel.updateShowDatePicker(false)
                    }
                ) {
                    Text(text = stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton( onClick = { viewModel.updateShowDatePicker(false) } ) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    Box(
        modifier = Modifier.clickable {
            viewModel.updateShowDatePicker(true)
        }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent {
                    if (it.isFocused) {
                        viewModel.updateShowDatePicker(true)
                    }
                },
            value = date?.let { formatDateFromMillis(it) } ?: "",
            onValueChange = {  },
            label = { Text(text = stringResource(R.string.transaction_date)) },
            readOnly = true,
            enabled = true,
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {  }
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null
                    )
                }
            }
        )
    }

}

@Preview
@Composable
fun TransactionTypePreview() {
    TransactionTypeSelector()
}