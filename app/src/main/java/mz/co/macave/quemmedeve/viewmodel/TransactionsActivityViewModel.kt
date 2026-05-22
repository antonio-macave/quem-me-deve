package mz.co.macave.quemmedeve.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import mz.co.macave.quemmedeve.data.repository.DebtRepository
import mz.co.macave.quemmedeve.data.repository.TransactionRepository
import mz.co.macave.quemmedeve.model.Transaction
import mz.co.macave.quemmedeve.util.DebtStatusCalculator
import mz.co.macave.quemmedeve.util.TransactionType
import mz.co.macave.quemmedeve.util.formatDate

class TransactionsActivityViewModel(
    val transactionRepository: TransactionRepository,
    val debtRepository: DebtRepository
) : ViewModel() {

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> get() = _date.asStateFlow()

    private val _debtAmount = MutableStateFlow(0.0)
    val debtAmount: StateFlow<Double> get() = _debtAmount.asStateFlow()

    private val _paidAmount = MutableStateFlow(0.0)
    val paidAmount: StateFlow<Double> get() = _paidAmount

    private val _debtorId = MutableStateFlow(0)
    val debtorId: StateFlow<Int> get() = _debtorId.asStateFlow()

    private val _debtId = MutableStateFlow(0)
    val debtId: StateFlow<Int> get() = _debtId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions: Flow<List<Transaction>> = _debtId
        .flatMapLatest { debtId ->
            if (debtId == 0) flowOf(emptyList())
            else transactionRepository.getTransactionsByDebtId(debtId)
        }


    fun updateDate(newDate: String) {
        _date.value = formatDate(newDate)
    }

    fun updateDebtAmount(newDebtBalance: Double) {
        _debtAmount.value = newDebtBalance
    }

    fun updatePaidAmount(newPaidAmount: Double) {
        _paidAmount.value = newPaidAmount
    }

    fun updateDebtorId(newDebtorId: Int) {
        _debtorId.value = newDebtorId
    }

    fun updateDebtId(newDebtId: Int) {
        _debtId.value = newDebtId
    }

    fun deleteTransaction(transaction: Transaction) {
        val newDebtAmount = when (transaction.type) {
            TransactionType.DEBIT.type ->
                _debtAmount.value - transaction.amount

            else ->
                _debtAmount.value
        }

        val newPaidAmount = when (transaction.type) {
            TransactionType.CREDIT.type ->
                _paidAmount.value - transaction.amount
            else ->
                _paidAmount.value
        }

        viewModelScope.launch {
            debtRepository.savePaidAmount(
                debtId = debtId.value,
                paidAmount = newPaidAmount,
                newDebtAmount = newDebtAmount,
                newDebtStatus = DebtStatusCalculator.calculate(
                    paidAmount = newPaidAmount,
                    debtAmount = newDebtAmount
                )
            )
            transactionRepository.deleteTransaction(transaction)
        }
    }

}