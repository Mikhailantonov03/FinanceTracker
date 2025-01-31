package com.devautro.financetracker.feature_payment.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devautro.financetracker.feature_moneySource.data.data_source.MoneySourceDao
import com.devautro.financetracker.feature_moneySource.data.model.MoneySourceEntity
import com.devautro.financetracker.feature_payment.data.model.PaymentEntity

@Database(
    entities = [
        PaymentEntity::class,
        MoneySourceEntity::class
    ],
    version = 1
)
abstract class PaymentsDatabase : RoomDatabase() {

    abstract val paymentDao: PaymentDao
    abstract val moneySourceDao: MoneySourceDao

    companion object {
        const val DATABASE_NAME = "payments_db"
    }
}