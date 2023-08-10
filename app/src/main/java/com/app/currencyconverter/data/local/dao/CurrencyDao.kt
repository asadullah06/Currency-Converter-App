package com.app.currencyconverter.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.currencyconverter.data.models.CurrencyResponse

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencies(currencyResponse: CurrencyResponse)

    @Query("select * from tbl_currencies_data")
    fun getCurrencyData(): CurrencyResponse?
}