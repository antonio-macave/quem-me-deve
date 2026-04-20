package mz.co.macave.whoowesme.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateDebtViewModel(
    val debtRepository: DebtRepository,
    debtorsRepository: DebtorRepository,
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name.asStateFlow()

    private val _surname = MutableStateFlow("")
    val surname: StateFlow<String> get() = _surname.asStateFlow()

    private val _dueDate = MutableStateFlow("")
    val dueDate: StateFlow<String> get() = _dueDate.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> get() = _description.asStateFlow()

    private val _additionalNotes = MutableStateFlow("")
    val additionalNotes: StateFlow<String> get() = _additionalNotes.asStateFlow()

    private val _showDueDateDialog = MutableStateFlow(false)
    val showDueDateDialog: StateFlow<Boolean> get() = _showDueDateDialog.asStateFlow()

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

    fun updateDueDate(newValue: String) {
        _dueDate.value = newValue
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

}