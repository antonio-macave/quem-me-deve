package mz.co.macave.quemmedeve.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Debt::class,
            parentColumns = ["id"],
            childColumns = ["debtId"],
            onDelete = CASCADE
        )
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: Int,
    val description: String,
    val amount: Double,
    val date: LocalDate,
    val debtId: Int
)