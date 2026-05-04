package mz.co.macave.whoowesme.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mz.co.macave.whoowesme.data.repository.DebtRepository
import mz.co.macave.whoowesme.data.repository.DebtorRepository
import mz.co.macave.whoowesme.data.repository.TransactionRepository
import mz.co.macave.whoowesme.util.formatDate

class TransactionsActivityViewModel(
    val transactionRepository: TransactionRepository,
    val debtRepository: DebtRepository,
    val debtorRepository: DebtorRepository
) : ViewModel() {

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> get() = _date.asStateFlow()

    private val _debtorId = MutableStateFlow(0)
    val debtorId: StateFlow<Int> get() = _debtorId.asStateFlow()

    private val _debtId = MutableStateFlow(0)
    val debtId: StateFlow<Int> get() = _debtId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions: Flow<List<Transaction>> = _debtId
        .flatMapLatest { id ->
            if (id == 0) flowOf(emptyList())
            else transactionRepository.getTransactionsByDebtId(id)
        }


    fun updateDate(newDate: String) {
        _date.value = formatDate(newDate)
    }


    fun updateDebtorId(newDebtorId: Int) {
        _debtorId.value = newDebtorId
    }

    fun updateDebtId(newDebtId: Int) {
        _debtId.value = newDebtId
    }

}