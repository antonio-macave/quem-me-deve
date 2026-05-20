package mz.co.macave.whoowesme.data.repository

import kotlinx.coroutines.flow.Flow
import mz.co.macave.whoowesme.data.dao.TransactionDao
import mz.co.macave.whoowesme.model.Transaction

class TransactionRepository(private val transactionDao: TransactionDao) {

    suspend fun saveTransaction(transaction: Transaction) {
        transactionDao.insertAll(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    fun getTransactionsByDebtId(debtId: Int): Flow<List<Transaction>> {
        return transactionDao.loadAllTransactionsByDebtId(debtId)
    }

    suspend fun findTransactionById(transactionIds: IntArray): List<Transaction> {
        return transactionDao.findTransactionById(transactionIds)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.update(transaction)
    }

}