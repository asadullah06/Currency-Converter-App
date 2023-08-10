package com.app.currencyconverter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.currencyconverter.data.local.dao.CurrencyDao
import com.app.currencyconverter.data.models.CurrencyResponse

@Database(entities = [CurrencyResponse::class], version = 1, exportSchema = false)
@TypeConverters(CurrenciesTypeConverter::class)
abstract class CurrenciesDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    companion object {

        const val DATABASE_NAME = "currencies_converter_app"
    }
}