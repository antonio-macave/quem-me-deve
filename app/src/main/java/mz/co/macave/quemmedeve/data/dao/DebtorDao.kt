package mz.co.macave.quemmedeve.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import mz.co.macave.quemmedeve.model.Debtor
import mz.co.macave.quemmedeve.model.DebtorWithDebts

@Dao
interface DebtorDao {

    @Query("SELECT * FROM debtors ORDER BY name ASC")
    fun getAllDebtors(): Flow<List<Debtor>>

    @Query("SELECT * FROM debtors")
    fun getAllDebtorsWithDebts(): Flow<List<DebtorWithDebts>>

    @Query("SELECT * FROM debtors WHERE id IN (:debtorIds)")
    suspend fun loadAllByIds(debtorIds: IntArray): List<Debtor>

    @Query("SELECT * FROM debtors WHERE name LIKE :name OR surname LIKE :surname LIMIT 1")
    suspend fun findByName(name: String, surname: String): Debtor

    @Query("SELECT * FROM debtors WHERE id = :debtorId LIMIT 1")
    suspend fun findDebtorById(debtorId: Int): Debtor

    @Transaction
    @Query("SELECT * FROM debtors WHERE id = :debtorId")
    suspend fun getDebtorWithDebts(debtorId: Int): List<DebtorWithDebts>

    @Insert
    suspend fun insertAll(vararg debtors: Debtor)

    @Update
    suspend fun update(debtor: Debtor)

    @Delete
    suspend fun delete(debtor: Debtor)
}