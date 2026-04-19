package mz.co.macave.whoowesme.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.viewmodel.CreateDebtorViewModel

@Composable
fun CreateDebtorContent(viewModel: CreateDebtorViewModel = viewModel()) {
    Column {
        NameAndSurnameFields(viewModel = viewModel)
        Spacer(Modifier.height(16.dp))
        ContactField(viewModel = viewModel)
    }
}

@Composable
fun ContactField(viewModel: CreateDebtorViewModel = viewModel()) {
    val number by viewModel.contactNumber.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        FieldIcon(imageVector = Icons.Default.Phone)

        Spacer(Modifier.width(8.dp))

        TextField(
            modifier = Modifier.weight(1f),
            value = number,
            onValueChange = {
                viewModel.updateContactNumber(it)
            },
            label = { Text(text = stringResource(R.string.contact_number)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun NameAndSurnameFields(viewModel: CreateDebtorViewModel = viewModel()) {

    Column {
        val name by viewModel.name.collectAsStateWithLifecycle()
        val surname by viewModel.surname.collectAsStateWithLifecycle()

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            FieldIcon(imageVector = Icons.Default.Person)

            Spacer(Modifier.width(8.dp))

            TextField(
                modifier = Modifier.weight(1f),
                value = name,
                onValueChange = {
                    viewModel.updateName(it)
                },
                label = { Text(text = stringResource(R.string.name)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            FieldIcon(imageVector = null)

            Spacer(Modifier.width(8.dp))

            TextField(
                modifier = Modifier.weight(1f),
                value = surname,
                onValueChange = {
                    viewModel.updateSurname(it)
                },
                label = { Text(text = stringResource(R.string.surname)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
        }
    }
}

@Composable
fun FieldIcon(imageVector: ImageVector?) {
    Box(contentAlignment = Alignment.Center) {
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
            )
        } else {
            Spacer(Modifier.width(32.dp))
        }

    }
}