package mz.co.macave.whoowesme.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mz.co.macave.whoowesme.data.dao.DebtDao
import mz.co.macave.whoowesme.data.dao.DebtorDao
import mz.co.macave.whoowesme.data.dao.TransactionDao
import mz.co.macave.whoowesme.model.Debt
import mz.co.macave.whoowesme.model.Debtor
import mz.co.macave.whoowesme.model.Transaction
import mz.co.macave.whoowesme.util.Converters

@Database(
    entities = [
        Debtor::class,
        Debt::class,
        Transaction::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun debtorDao(): DebtorDao
    abstract fun debtDao(): DebtDao
    abstract fun transactionDao(): TransactionDao

}