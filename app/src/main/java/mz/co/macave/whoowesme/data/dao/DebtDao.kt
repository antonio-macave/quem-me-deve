package mz.co.macave.whoowesme.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mz.co.macave.whoowesme.model.Debt
import mz.co.macave.whoowesme.model.DebtCardItem

@Dao
interface DebtDao {

    @Query("SELECT * FROM debts")
    fun getAllDebts(): Flow<List<Debt>>

    @Query("""
        SELECT 
            debts.id AS debtId, 
            debtors.id AS debtorId, 
            debtors.name AS debtorName,
            debtors.surname AS debtorSurname,
            debts.amount AS amount,
            debts.paidAmount AS paidAmount,
            debts.status AS status,
            debts.description AS description,
            debts.additionalNotes AS additionalNotes,
            debts.dueTo AS dueTo
        FROM debts
        INNER JOIN debtors ON debts.debtorId = debtors.id
        """)
    fun findDebtsWithDebtorName(): Flow<List<DebtCardItem>>

    @Query("SELECT * FROM debts WHERE id IN (:debtIds)")
    suspend fun loadAllDebtsById(debtIds: IntArray): List<Debt>

    @Query("UPDATE debts SET paidAmount = :paidAmount WHERE id = :debtId")
    suspend fun savePaidAmount(debtId: Int, paidAmount: Double)

    @Insert
    suspend fun insertAll(vararg debts: Debt)

    @Delete
    suspend fun delete(debt: Debt)

}