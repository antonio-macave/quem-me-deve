package mz.co.macave.whoowesme.ui.screen

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mz.co.macave.whoowesme.R
import mz.co.macave.whoowesme.model.Transaction
import mz.co.macave.whoowesme.util.formatDate
import java.time.LocalDate

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Today,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = formatDate(transaction.date.toString()),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconAndDescription(R.drawable.outline_money_24, transaction.amount.toString())
            Spacer(Modifier.height(4.dp))
            IconAndDescription(R.drawable.description_24, transaction.description)
        }
    }
}


@Composable
fun TransactionsList(transactions: List<Transaction>) {
    LazyColumn {
        itemsIndexed(items = transactions) { index, item ->
            TransactionItem(transaction = item)
        }
    }
}

@Composable
fun IconAndDescription(@DrawableRes iconRes: Int, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(14.dp),
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
