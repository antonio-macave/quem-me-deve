package mz.co.macave.whoowesme.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mz.co.macave.whoowesme.model.Transaction

@Dao
interface TransactionDao {

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE debtId = :debtId")
    fun loadAllTransactionsByDebtId(debtId: Int): Flow<List<Transaction>>

    @Insert
    suspend fun insertAll(vararg transactions: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}