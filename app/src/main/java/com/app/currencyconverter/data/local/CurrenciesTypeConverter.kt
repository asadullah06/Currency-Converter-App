package com.app.currencyconverter.data.local

import androidx.room.TypeConverter
import com.app.currencyconverter.data.models.Rates
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CurrenciesTypeConverter {

    @TypeConverter
    @JvmStatic
    fun fromCurrency(value: Rates): String = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun toCurrency(value: String): Rates {
        val type = object : TypeToken<Rates>() {}.type
        return Gson().fromJson(value, type)
    }
}