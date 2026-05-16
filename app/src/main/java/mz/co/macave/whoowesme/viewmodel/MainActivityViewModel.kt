package mz.co.macave.whoowesme.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mz.co.macave.whoowesme.data.repository.DebtRepository
import mz.co.macave.whoowesme.model.DebtCardItem
import mz.co.macave.whoowesme.util.DebtStatus
import mz.co.macave.whoowesme.util.SortOption
import java.time.LocalDate

class MainActivityViewModel(debtRepository: DebtRepository): ViewModel() {

    private val _fabMenuExpanded = MutableStateFlow(false)
    val fabMenuExpanded: StateFlow<Boolean> get() = _fabMenuExpanded.asStateFlow()

    private val _showSortDebtsDialog = MutableStateFlow(false)
    val showSortDebtsDialog: StateFlow<Boolean> get() = _showSortDebtsDialog.asStateFlow()

    private val _overflowMenuExpanded = MutableStateFlow(false)
    val overflowMenuExpanded: StateFlow<Boolean> get() = _overflowMenuExpanded.asStateFlow()

    val debts = debtRepository.findDebtsWithDebtorName()


    fun sortDebts(debts: List<DebtCardItem>, sortBy: Int): List<DebtCardItem> {
        return when (sortBy) {
            SortOption.NAME.option -> debts.sortedBy { it.description }
            SortOption.DATE.option -> debts.sortedBy { it.dueTo }
            SortOption.AMOUNT.option -> debts.sortedBy { it.amount }
            else -> debts
        }
    }

    fun filterDebts(debts: List<DebtCardItem>, all: Boolean, pending: Boolean, paid: Boolean, overdue: Boolean): List<DebtCardItem> {
        return debts.filter { debt ->
            when (debt.status) {
                DebtStatus.PENDING.code -> pending
                DebtStatus.PAID.code -> paid
                DebtStatus.OVERDUE.code -> overdue
                else -> all
            }
        }
    }

    fun getDebtStatus(dueTo: LocalDate): Int {
        val now = LocalDate.now()
        return when {
            now.isAfter(dueTo) -> DebtStatus.OVERDUE.code
            now.isBefore(dueTo) -> DebtStatus.PENDING.code
            else -> DebtStatus.PAID.code
        }
    }

    fun isPaymentOverDue(dueTo: LocalDate): Boolean {
        val now = LocalDate.now()
        return now.isAfter(dueTo)
    }

    fun updateShowSortDebtsDialog(show: Boolean) {
        _showSortDebtsDialog.value = show
    }


    fun updateFabMenuExpanded(expanded: Boolean) {
        _fabMenuExpanded.value = expanded
    }


    fun updateOverflowMenuExpanded(expanded: Boolean) {
        _overflowMenuExpanded.value = expanded
    }
}