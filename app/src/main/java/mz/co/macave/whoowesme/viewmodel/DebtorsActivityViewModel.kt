package mz.co.macave.whoowesme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import mz.co.macave.whoowesme.data.repository.DebtorRepository

class DebtorsActivityViewModel(debtorsRepository: DebtorRepository) : ViewModel() {

    private val _cardExpanded = MutableStateFlow<Int?>(null)
    val cardExpanded: StateFlow<Int?> get() = _cardExpanded.asStateFlow()

    val debtors = debtorsRepository.getAllDebtors()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateCardExpanded(id: Int) {
        _cardExpanded.value = if (_cardExpanded.value == id) null else id
    }
}