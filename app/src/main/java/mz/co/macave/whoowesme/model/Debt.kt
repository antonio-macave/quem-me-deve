package mz.co.macave.whoowesme.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "debts")
data class Debt (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val status: Int,
    val description: String,
    val additionalNotes: String,
    val amount: Double,
    val paidAmount: Double,
    val dueTo: LocalDate,
    val debtorId: Int
)