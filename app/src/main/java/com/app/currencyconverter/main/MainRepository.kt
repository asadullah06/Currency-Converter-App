package com.app.currencyconverter.main

import com.app.currencyconverter.data.models.CurrencyResponse
import com.app.currencyconverter.utils.Resource

interface MainRepository {

    suspend fun getRemoteCurrencyRates():Resource<CurrencyResponse>

    suspend fun getRatesLocally():Resource<CurrencyResponse>

}