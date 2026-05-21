package mz.co.macave.quemmedeve.data.repository

import kotlinx.coroutines.flow.Flow
import mz.co.macave.quemmedeve.data.dao.DebtDao
import mz.co.macave.quemmedeve.model.Debt
import mz.co.macave.quemmedeve.model.DebtCardItem

class DebtRepository(private val debtDao: DebtDao) {

    suspend fun saveDebt(debt: Debt) {
        debtDao.insertAll(debt)
    }

    suspend fun savePaidAmount(debtId: Int, paidAmount: Double, newDebtAmount: Double) {
        debtDao.savePaidAmount(debtId, paidAmount, newDebtAmount)
    }

    suspend fun findDebtById(debtId: Int): Debt {
        return debtDao.findDebtById(debtId)
    }

    fun getAllDebts(): Flow<List<Debt>> {
        return debtDao.getAllDebts()
    }

    suspend fun updateDebt(debt: Debt) {
        debtDao.update(debt)
    }

    fun findDebtsWithDebtorName(): Flow<List<DebtCardItem>> {
        return debtDao.findDebtsWithDebtorName()
    }


    suspend fun findDebtsByDebtorId(debtorId: IntArray): List<Debt> {
        return debtDao.loadAllDebtsById(debtorId)
    }

    suspend fun deleteDebt(debt: Debt) {
        debtDao.delete(debt)
    }

    suspend fun deleteById(debtId: Int) {
        debtDao.deleteById(debtId)
    }

}