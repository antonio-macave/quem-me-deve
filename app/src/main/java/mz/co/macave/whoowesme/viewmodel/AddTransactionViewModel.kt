package mz.co.macave.whoowesme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mz.co.macave.whoowesme.data.repository.TransactionRepository
import mz.co.macave.whoowesme.model.Transaction

class AddTransactionViewModel : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> get() = _amount.asStateFlow()

    private val _transactionType = MutableStateFlow<Int>(0)
    val transactionType: StateFlow<Int> get() = _transactionType.asStateFlow()

    private val _debtId = MutableStateFlow<Int?>(null)
    val debtId: StateFlow<Int?> get() = _debtId.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> get() = _description.asStateFlow()


    private val _isTotalPayment = MutableStateFlow(false)
    val isTotalPayment: StateFlow<Boolean> get() = _isTotalPayment.asStateFlow()

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> get() = _showDatePicker.asStateFlow()

    private val _transactionDate = MutableStateFlow<Long?>(null)
    val transactionDate: StateFlow<Long?> get() = _transactionDate.asStateFlow()

    fun updateShowDatePicker(value: Boolean) {
        _showDatePicker.value = value
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
    fun saveTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.saveTransaction(transaction)
        }
    }

}