package mz.co.macave.whoowesme.data.repository

import kotlinx.coroutines.flow.Flow
import mz.co.macave.whoowesme.data.dao.DebtDao
import mz.co.macave.whoowesme.model.Debt

class DebtRepository(private val debtDao: DebtDao) {

    suspend fun saveDebt(debt: Debt) {
        debtDao.insertAll(debt)
    }

    fun getAllDebts(): Flow<List<Debt>> {
        return debtDao.getAllDebts()
    }

    suspend fun findDebtsByDebtorId(debtorId: IntArray): List<Debt> {
        return debtDao.loadAllDebtsById(debtorId)
    }

    suspend fun deleteDebt(debt: Debt) {
        debtDao.delete(debt)
    }

}