package mz.co.macave.whoowesme.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import mz.co.macave.whoowesme.model.Debt
import mz.co.macave.whoowesme.model.Debtor
import mz.co.macave.whoowesme.model.Transaction

@Database(
    entities = [
        Debtor::class,
        Debt::class,
        Transaction::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun debtorDao(): DebtorDao
    abstract fun debtDao(): DebtDao
    abstract fun transactionDao(): TransactionDao

}