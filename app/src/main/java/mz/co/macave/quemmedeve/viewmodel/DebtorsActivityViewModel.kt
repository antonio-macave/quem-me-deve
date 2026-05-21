package mz.co.macave.quemmedeve.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import mz.co.macave.quemmedeve.data.repository.DebtorRepository
import mz.co.macave.quemmedeve.model.Debtor
import mz.co.macave.quemmedeve.model.DebtorWithDebts

class DebtorsActivityViewModel(val debtorsRepository: DebtorRepository) : ViewModel() {

    private val _cardExpanded = MutableStateFlow<Int?>(null)
    val cardExpanded: StateFlow<Int?> get() = _cardExpanded.asStateFlow()

    val debtors = debtorsRepository.getAllDebtors()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val debtorsWithDebts = debtorsRepository.getAllDebtorsWithDebts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun updateCardExpanded(id: Int) {
        _cardExpanded.value = if (_cardExpanded.value == id) null else id
    }

    fun getTotalDebt(debtorWithDebts: DebtorWithDebts): Double {
        return debtorWithDebts.debts.sumOf { it.amount - it.paidAmount }
    }

    fun deleteDebtor(debtor: Debtor) {
        viewModelScope.launch {
            debtorsRepository.deleteDebtor(debtor)
        }
    }
}