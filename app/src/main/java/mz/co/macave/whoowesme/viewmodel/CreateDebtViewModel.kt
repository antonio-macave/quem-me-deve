package mz.co.macave.whoowesme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mz.co.macave.whoowesme.data.repository.DebtRepository
import mz.co.macave.whoowesme.data.repository.DebtorRepository
import mz.co.macave.whoowesme.model.Debt

class CreateDebtViewModel(
    val debtRepository: DebtRepository,
    debtorsRepository: DebtorRepository,
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name.asStateFlow()

    private val _surname = MutableStateFlow("")
    val surname: StateFlow<String> get() = _surname.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> get() = _description.asStateFlow()

    private val _additionalNotes = MutableStateFlow("")
    val additionalNotes: StateFlow<String> get() = _additionalNotes.asStateFlow()

    private val _showDueDateDialog = MutableStateFlow(false)
    val showDueDateDialog: StateFlow<Boolean> get() = _showDueDateDialog.asStateFlow()

    private val _dueToDate = MutableStateFlow<Long?>(null)
    val dueToDate: StateFlow<Long?> get() = _dueToDate.asStateFlow()

    private val _selectedDebtor = MutableStateFlow<Debtor?>(null)
    val selectedDebtor: StateFlow<Debtor?> get() = _selectedDebtor.asStateFlow()


    val debtors = debtorsRepository.getAllDebtors()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    fun areAllFieldsFilled(): Boolean {
        return false
    }

    fun areDebtorDetailsFilled(): Boolean {
        return name.value.isEmpty() && surname.value.isEmpty()
    }

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> get() = _amount.asStateFlow()

    fun updateName(newValue: String) {
        _name.value = newValue
    }

    fun updateSurname(newValue: String) {
        _surname.value = newValue
    }

    fun updateDescription(newValue: String) {
        _description.value = newValue
    }

    fun updateShowDueDateDialog(newValue: Boolean) {
        _showDueDateDialog.value = newValue
    }

    fun updateAdditionalNotes(newValue: String) {
        _additionalNotes.value = newValue
    }

    fun updateAmount(amount: String) {
        _amount.value = amount
    }

    fun updateSelectedDebtor(debtor: Debtor) {
        _selectedDebtor.value = debtor
    }

    fun isDueDateSelected(): Boolean {
        return _dueToDate.value != null
    }

    fun isDebtorSelected(): Boolean {
        return _selectedDebtor.value != null
    }

    fun saveDebt(debt: Debt) {
        if (isDebtorSelected() && isDueDateSelected()) {
            viewModelScope.launch {
                debtRepository.saveDebt(debt)
            }
        }
    }

}