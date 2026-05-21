package mz.co.macave.quemmedeve.data.repository

import kotlinx.coroutines.flow.Flow
import mz.co.macave.quemmedeve.data.dao.DebtorDao
import mz.co.macave.quemmedeve.model.Debtor
import mz.co.macave.quemmedeve.model.DebtorWithDebts

class DebtorRepository(private val debtorDao: DebtorDao) {

    suspend fun saveDebtor(debtor: Debtor) {
        debtorDao.insertAll(debtor)
    }

    suspend fun deleteDebtor(debtor: Debtor) {
        debtorDao.delete(debtor)
    }

    suspend fun updateDebtor(debtor: Debtor) {
        return debtorDao.update(debtor)
    }

    suspend fun findDebtorByNameAndSurname(name: String, surname: String): Debtor {
        return debtorDao.findByName(name, surname)
    }

    suspend fun findDebtorById(debtorId: Int): Debtor {
        return debtorDao.findDebtorById(debtorId)
    }

    suspend fun getDebtorsWithDebts(debtorId: Int): List<DebtorWithDebts> {
        return debtorDao.getDebtorWithDebts(debtorId)
    }

    fun getAllDebtors(): Flow<List<Debtor>> {
        return debtorDao.getAllDebtors()
    }

    fun getAllDebtorsWithDebts(): Flow<List<DebtorWithDebts>> {
        return debtorDao.getAllDebtorsWithDebts()
    }

    suspend fun findDebtorsById(debtorIds: IntArray): List<Debtor> {
        return debtorDao.loadAllByIds(debtorIds)
    }

}