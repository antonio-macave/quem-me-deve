package mz.co.macave.whoowesme.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.util.formatDateFromMillis
import mz.co.macave.whoowesme.viewmodel.CreateDebtViewModel

@Composable
fun CreateDebt(viewModel: CreateDebtViewModel) {
    //NameFields()
    AmountField(viewModel)
    DueDate(viewModel) {  }
    DebtorSituationSelector {

    }
    DescriptionField(viewModel)
    AdditionalNotesField(viewModel)
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DebtorSituationSelector(viewModel: CreateDebtViewModel = viewModel(), onOptionIndexSelected: (Int) -> Unit) {

    val options = listOf(stringResource(R.string.existing), stringResource(R.string.new_one))
    var selectedIndex by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
    ) {
        val modifiers = listOf(Modifier.weight(1f), Modifier.weight(1f))
        options.forEachIndexed { index, option ->
            ToggleButton(
                modifier = modifiers[index].semantics { role = Role.RadioButton },
                checked = (index == selectedIndex),
                onCheckedChange = {
                    selectedIndex = index
                    onOptionIndexSelected(index)
                },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                }
            ) {
                AnimatedVisibility(index == selectedIndex) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(ToggleButtonDefaults.IconSpacing))
                Text(text = options[index])
            }
        }
    }
    when (selectedIndex) {
        0 -> ExistingDebtorSelector(viewModel = viewModel)
        1 -> NameFields()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingDebtorSelector(viewModel: CreateDebtViewModel) {

    var text by remember { mutableStateOf("") }
    val suggestions by viewModel.debtors.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {

            TextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true),
                value = text,
                onValueChange = { text = it },
                label = { Text(text = stringResource(R.string.debtor)) },
                singleLine = true,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                suggestions.forEach { item ->
                    DropdownMenuItem(
                        modifier = Modifier.background(color = if ("${item.name} ${item.surname}" == text) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.background),
                        text = { Text(text = "${item.name} ${item.surname}") },
                        leadingIcon = { if ("${item.name} ${item.surname}" == text) { Icon(imageVector = Icons.Default.Check, contentDescription = null) } },
                        onClick = {
                            text = "${item.name} ${item.surname}"
                            viewModel.updateSelectedDebtor(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AmountField(viewModel: CreateDebtViewModel) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {

        val amountText by viewModel.amount.collectAsStateWithLifecycle()
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            onValueChange = { text: String ->
                val filteredValue = text.replace(".",",")
                viewModel.updateAmount(filteredValue)
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_money_24),
                    contentDescription = null
                )
            },
            value = amountText,
            singleLine = true,
            label = { Text(text = stringResource(R.string.amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text(text = "MZN") }
        )
    }
}

@Composable
fun DueDate(viewModel: CreateDebtViewModel, onDialogRequestListener: () -> Unit) {
    val date by viewModel.dueToDate.collectAsStateWithLifecycle()
    val showDialog by viewModel.showDueDateDialog.collectAsStateWithLifecycle()

    if (showDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = onDialogRequestListener,
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.pickDate(datePickerState.selectedDateMillis)
                        viewModel.updateShowDueDateDialog(false)
                    }
                ) {
                    Text(text = stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.updateShowDueDateDialog(false) }
                ) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    TextField(
        modifier = Modifier
            .width(256.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                viewModel.updateShowDueDateDialog(true)
            },
        value = date,
        readOnly = true,
        onValueChange = { },
        label = { Text(text = stringResource(R.string.due_date)) },
        singleLine = true,
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable {
                    viewModel.updateShowDueDateDialog(true)
                },
                imageVector = Icons.Default.DateRange,
                contentDescription = null
            )
        }
    )
}

@Composable
fun NameFields() {
    Column {

        var name by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }

        TextField(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            value = name,
            onValueChange = {
                name = it
            },
            label = { Text(text = stringResource(R.string.name)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
        )

        TextField(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            value = surname,
            onValueChange = {
                surname = it
            },
            label = { Text(text = stringResource(R.string.surname)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            )
        )
    }
}

@Composable
fun DescriptionField(viewModel: CreateDebtViewModel) {
    val description by viewModel.description.collectAsStateWithLifecycle()
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        value = description,
        label = { Text(text = stringResource(R.string.description)) },
        onValueChange = { viewModel.updateDescription(it) },
        minLines = 3,
        maxLines = 3
    )
}

@Composable
fun AdditionalNotesField(viewModel: CreateDebtViewModel) {
    val additionalNotes by viewModel.additionalNotes.collectAsStateWithLifecycle()
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        value = additionalNotes,
        label = { Text(text = stringResource(R.string.additional_notes)) },
        onValueChange = { viewModel.updateAdditionalNotes(it) },
        minLines = 4,
        maxLines = 4
    )
}

@Preview
@Composable
fun CreateDebtPreviews() {
    //AmountField()
    //NameFields()
    DescriptionField()
    ExistingDebtorSelector {  }
}