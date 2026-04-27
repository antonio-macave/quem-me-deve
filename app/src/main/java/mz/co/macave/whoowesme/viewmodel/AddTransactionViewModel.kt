package mz.co.macave.whoowesme.viewmodel

import androidx.lifecycle.ViewModel

class AddTransactionViewModel : ViewModel() {

    private val _amount = MutableStateFlow(0)
    val amount: StateFlow<Int> get() = _amount.asStateFlow()

    private val _transactionType = MutableStateFlow<Int>(0)
    val transactionType: StateFlow<Int> get() = _transactionType.asStateFlow()

    private val _debtId = MutableStateFlow<Int?>(null)
    val debtId: StateFlow<Int?> get() = _debtId.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> get() = _description.asStateFlow()


    private val _isTotalPayment = MutableStateFlow(false)
    val isTotalPayment: StateFlow<Boolean> get() = _isTotalPayment.asStateFlow()

    fun updateIsTotalPayment(value: Boolean) {
        _isTotalPayment.value = value
    }

    fun updateAmount(value: Int) {
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

}