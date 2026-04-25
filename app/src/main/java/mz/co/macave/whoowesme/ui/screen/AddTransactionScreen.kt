package mz.co.macave.whoowesme.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEachIndexed
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.util.TransactionType


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TransactionTypeSelector() {
    val options = TransactionType.entries
    var selectedOption by remember { mutableIntStateOf(0) }
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
                onCheckedChange = { selectedOption = index },
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
                    text = when(item) {
                        TransactionType.DEBIT -> stringResource(R.string.transaction_type_debit)
                        TransactionType.CREDIT -> stringResource(R.string.transaction_type_credit)
                    }
                )
            }
        }
    }
}

@Composable
fun TotalPaymentSwitch() {
    var selected by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .selectable(
                selected = selected,
                onClick = { selected = !selected },
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
            checked = selected,
            onCheckedChange = { selected = !selected }
        )
    }
}

@Preview
@Composable
fun TransactionTypePreview() {
    TransactionTypeSelector()
}