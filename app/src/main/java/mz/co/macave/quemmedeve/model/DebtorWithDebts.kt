package mz.co.macave.quemmedeve.model

import androidx.room.Embedded
import androidx.room.Relation

data class DebtorWithDebts(
    @Embedded val debtor: Debtor,
    @Relation(
        parentColumn = "id",
        entityColumn = "debtorId"
    )
    val debts: List<Debt>
)