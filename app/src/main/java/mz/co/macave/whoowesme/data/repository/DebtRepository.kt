package mz.co.macave.whoowesme.data.repository

import kotlinx.coroutines.flow.Flow
import mz.co.macave.whoowesme.data.dao.DebtDao
import mz.co.macave.whoowesme.model.Debt
import mz.co.macave.whoowesme.model.DebtCardItem

class DebtRepository(private val debtDao: DebtDao) {

    suspend fun saveDebt(debt: Debt) {
        debtDao.insertAll(debt)
    }

    suspend fun savePaidAmount(debtId: Int, paidAmount: Double) {
        debtDao.savePaidAmount(debtId, paidAmount)
    }

    fun getAllDebts(): Flow<List<Debt>> {
        return debtDao.getAllDebts()
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

}