package mz.co.macave.quemmedeve.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mz.co.macave.quemmedeve.data.repository.DebtRepository
import mz.co.macave.quemmedeve.model.DebtCardItem
import mz.co.macave.quemmedeve.util.DebtStatus
import mz.co.macave.quemmedeve.util.SortOption
import java.time.LocalDate

class MainActivityViewModel(val debtRepository: DebtRepository): ViewModel() {

    private val _fabMenuExpanded = MutableStateFlow(false)
    val fabMenuExpanded: StateFlow<Boolean> get() = _fabMenuExpanded.asStateFlow()

    private val _showSortDebtsDialog = MutableStateFlow(false)
    val showSortDebtsDialog: StateFlow<Boolean> get() = _showSortDebtsDialog.asStateFlow()

    private val _sortByOption = MutableStateFlow(SortOption.DATE)
    val sortByOption: StateFlow<SortOption> get() = _sortByOption.asStateFlow()

    private val _overflowMenuExpanded = MutableStateFlow(false)
    val overflowMenuExpanded: StateFlow<Boolean> get() = _overflowMenuExpanded.asStateFlow()

    val debts = debtRepository.findDebtsWithDebtorName()


    fun sortDebts(debts: List<DebtCardItem>, sortBy: SortOption): List<DebtCardItem> {
        return when (sortBy) {
            SortOption.NAME -> debts.sortedBy { it.debtorName }
            SortOption.DATE -> debts.sortedBy { it.dueTo }
            SortOption.AMOUNT -> debts.sortedBy { it.amount }
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

    fun updateSortOption(option: SortOption) {
        _sortByOption.value = option
    }


    fun updateOverflowMenuExpanded(expanded: Boolean) {
        _overflowMenuExpanded.value = expanded
    }

    fun deleteDebtById(debtId: Int) {
        viewModelScope.launch {
            debtRepository.deleteById(debtId)
        }
    }
}