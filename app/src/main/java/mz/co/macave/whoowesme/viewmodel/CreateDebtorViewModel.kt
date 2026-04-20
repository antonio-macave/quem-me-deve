package mz.co.macave.whoowesme.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateDebtorViewModel(val debtorRepository: DebtorRepository) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _surname = MutableStateFlow("")
    val surname: StateFlow<String> = _surname.asStateFlow()

    private val _contactNumber = MutableStateFlow("")
    val contactNumber: StateFlow<String> = _contactNumber.asStateFlow()


    val debtors = debtorRepository.getAllDebtors()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateSurname(newSurname: String) {
        _surname.value = newSurname
    }

    fun updateContactNumber(newContactNumber: String) {
        _contactNumber.value = newContactNumber
    }

    fun validateData(name: String, surname: String, contactNumber: String): Boolean {
        return name.isNotEmpty() && surname.isNotEmpty() && contactNumber.isNotEmpty()
    }

    fun saveDebtor(name: String, surname: String, contactNumber: String) {
        if (validateData(name, surname, contactNumber)) {
            val debtor = Debtor(name = name, surname = surname, contactNumber = contactNumber)
            viewModelScope.launch {
                debtorRepository.saveDebtor(debtor)
                println("Debtor saved: $debtor")
            }
        }
    }
}