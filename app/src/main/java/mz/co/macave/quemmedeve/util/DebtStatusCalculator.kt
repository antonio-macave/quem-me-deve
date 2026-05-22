package mz.co.macave.quemmedeve.util

object DebtStatusCalculator {

    fun calculate(
        paidAmount: Double,
        debtAmount: Double
    ): Int {
        return when {
            paidAmount <= 0.0 ->
                DebtStatus.PENDING.code

            paidAmount >= debtAmount ->
                DebtStatus.PAID.code

            else ->
                DebtStatus.PENDING.code
        }
    }

}