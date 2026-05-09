package mz.co.macave.whoowesme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mz.co.macave.whoowesme.data.repository.DebtRepository
import mz.co.macave.whoowesme.data.repository.TransactionRepository
import mz.co.macave.whoowesme.model.Transaction
import mz.co.macave.whoowesme.util.TransactionType

class AddTransactionViewModel(
    val transactionRepository: TransactionRepository,
    val debtRepository: DebtRepository
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> get() = _amount.asStateFlow()

    private val _transactionType = MutableStateFlow(0)
    val transactionType: StateFlow<Int> get() = _transactionType.asStateFlow()

    private val _debtId = MutableStateFlow<Int?>(null)
    val debtId: StateFlow<Int?> get() = _debtId.asStateFlow()

    private val _remainingBalance = MutableStateFlow(0.0)
    val remainingBalance: StateFlow<Double> get() = _remainingBalance.asStateFlow()

    private val _paidAmount = MutableStateFlow(0.0)
    val paidAmount: StateFlow<Double> get() = _paidAmount.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> get() = _description.asStateFlow()

    private val _debtBalance = MutableStateFlow(0.0)
    val debtBalance: StateFlow<Double> get() = _debtBalance.asStateFlow()

    private val _isTotalPayment = MutableStateFlow(false)
    val isTotalPayment: StateFlow<Boolean> get() = _isTotalPayment.asStateFlow()

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> get() = _showDatePicker.asStateFlow()

    private val _transactionDate = MutableStateFlow<Long?>(null)
    val transactionDate: StateFlow<Long?> get() = _transactionDate.asStateFlow()

    fun updateDebtBalance(value: Double) {
        _debtBalance.value = value
    }

    fun updateShowDatePicker(value: Boolean) {
        _showDatePicker.value = value
    }

    fun updateDebtAmount(value: Double) {
        _debtAmount.value = value
    }

    fun updateRemainingBalance(value: Double) {
        _remainingBalance.value = value
    }

    fun updateSelectedDate(value: Long) {
        _transactionDate.value = value
    }

    fun updateIsTotalPayment(value: Boolean) {
        _isTotalPayment.value = value
    }

    fun updateAmount(value: String) {
        _amount.value = value
    }

    fun updateTransactionType(value: Int) {
        _transactionType.value = value
    }

    fun updateDebtId(value: Int) {
        _debtId.value = value
    }


    fun updateDescription(value: String) {
        _description.value = value
    }

    fun updateRemainingAmount(remainingAmount: Double) {
        _remainingBalance.value = remainingAmount
    }

    fun calculateRemainingBalance(amount: Double) {
        val balance =
            when (transactionType.value) {
                TransactionType.CREDIT.type -> {
                    _remainingBalance.value = debtAmount.value - paidAmount.value - amount
                }
                TransactionType.DEBIT.type -> {
                    _remainingBalance.value = debtAmount.value - paidAmount.value + amount
                }
                else -> {
                    _remainingBalance.value = debtAmount.value - paidAmount.value
                }
            }
    }

    fun saveTransaction(transaction: Transaction) {
        val newPaidAmount = when (transaction.type) {
            TransactionType.DEBIT.ordinal -> _paidAmount.value + transaction.amount
            TransactionType.CREDIT.ordinal -> _paidAmount.value - transaction.amount
            else -> _debtAmount.value
        }
        viewModelScope.launch {
            transactionRepository.saveTransaction(transaction)
            debtRepository.savePaidAmount(debtId = debtId.value!!, paidAmount = newPaidAmount)
        }
    }

}