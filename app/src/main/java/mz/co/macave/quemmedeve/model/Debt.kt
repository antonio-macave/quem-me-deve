package mz.co.macave.quemmedeve.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "debts",
    foreignKeys = [
        ForeignKey(
            entity = Debtor::class,
            parentColumns = ["id"],
            childColumns = ["debtorId"],
            onDelete = CASCADE
        )
    ]
)
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