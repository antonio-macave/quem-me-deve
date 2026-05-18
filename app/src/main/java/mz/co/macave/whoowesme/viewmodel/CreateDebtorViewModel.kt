package mz.co.macave.whoowesme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mz.co.macave.whoowesme.data.repository.DebtorRepository
import mz.co.macave.whoowesme.model.Debtor
import kotlin.collections.emptyList

class CreateDebtorViewModel(val debtorRepository: DebtorRepository) : ViewModel() {

    private var debtorId = -1

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

    fun updateDebtorId(newValue: Int) {
        debtorId = newValue
    }

    fun updateSurname(newSurname: String) {
        _surname.value = newSurname
    }

    fun updateContactNumber(newContactNumber: String) {
        _contactNumber.value = newContactNumber
    }

    fun validateData(name: String, surname: String): Boolean {
        return name.isNotEmpty() && surname.isNotEmpty()
    }

    fun saveDebtor(name: String, surname: String, contactNumber: String) {
        if (validateData(name, surname)) {
            val debtor = Debtor(name = name, surname = surname, contactNumber = contactNumber)
            viewModelScope.launch {
                debtorRepository.saveDebtor(debtor)
                println("Debtor saved: $debtor")
            }
        }
    }

    fun updateDebtor(debtor: Debtor) {
        viewModelScope.launch {
            debtorRepository.updateDebtor(debtor)
        }
    }

    fun loadDebtor(debtorId: Int) {
        viewModelScope.launch {
            val debtor = debtorRepository.findDebtorsById(intArrayOf(debtorId))
            _name.value = debtor.first().name
            _surname.value = debtor.first().surname
            _contactNumber.value = debtor.first().contactNumber
        }
    }


    fun save() {
        val debtor = Debtor(
            id = debtorId,
            name = name.value,
            surname = surname.value,
            contactNumber = contactNumber.value
        )
        if (debtorId != -1) {
            updateDebtor(debtor)
        } else {
            saveDebtor()
        }
    }
}