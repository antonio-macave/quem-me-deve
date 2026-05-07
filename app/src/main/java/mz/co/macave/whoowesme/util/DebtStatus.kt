package mz.co.macave.whoowesme.util

enum class DebtStatus(val code: Int) {
    PAID(1),
    PENDING(0),
    OVERDUE(-1),
}