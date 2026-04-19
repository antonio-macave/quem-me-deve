package mz.co.macave.whoowesme.data.repository

import mz.co.macave.whoowesme.data.dao.DebtorDao
import mz.co.macave.whoowesme.model.Debtor
import mz.co.macave.whoowesme.model.DebtorWithDebts

class DebtorRepository(private val debtorDao: DebtorDao) {

    suspend fun saveDebtor(debtor: Debtor) {
        debtorDao.insertAll(debtor)
    }

    suspend fun deleteDebtor(debtor: Debtor) {
        debtorDao.delete(debtor)
    }

    suspend fun findDebtorByNameAndSurname(name: String, surname: String): Debtor {
        return debtorDao.findByName(name, surname)
    }

    suspend fun getDebtorsWithDebts(debtorId: Int): List<DebtorWithDebts> {
        return debtorDao.getDebtorWithDebts(debtorId)
    }

    fun getAllDebtors(): Flow<List<Debtor>> {
        return debtorDao.getAllDebtors()
    }

    suspend fun findDebtorsById(debtorIds: IntArray): List<Debtor> {
        return debtorDao.loadAllByIds(debtorIds)
    }

}