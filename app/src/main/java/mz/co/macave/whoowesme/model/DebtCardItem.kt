package mz.co.macave.whoowesme.model

import java.time.LocalDate

data class DebtCardItem(
    val debtId: Int,
    val debtorId: Int,
    val debtorName: String,
    val debtorSurname: String,
    val amount: Double,
    val paidAmount: Double,
    val status: Int,
    val description: String,
    val additionalNotes: String,
    val dueTo: LocalDate,
)