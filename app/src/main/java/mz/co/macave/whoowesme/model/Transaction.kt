package mz.co.macave.whoowesme.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: Int,
    val description: String,
    val amount: Double,
    val date: LocalDate,
    val debtId: Int
)