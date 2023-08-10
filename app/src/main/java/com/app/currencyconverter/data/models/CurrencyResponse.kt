package com.app.currencyconverter.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.app.currencyconverter.data.local.CurrenciesTypeConverter

@Entity(tableName = "tbl_currencies_data")
data class CurrencyResponse(
    @PrimaryKey
    val page: Int = 1,

    @ColumnInfo(name = "base_currency_name")
    val base: String,
    @ColumnInfo(name = "disclaimer")
    val disclaimer: String,
    @ColumnInfo(name = "license_info")
    val license: String,
    @TypeConverters(CurrenciesTypeConverter::class)
    @ColumnInfo(name = "currency_rates")
    val rates: Rates?,
    @ColumnInfo(name = "utc_timestamp")
    val timestamp: Int
)