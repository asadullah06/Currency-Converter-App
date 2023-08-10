package com.app.currencyconverter.main

import com.app.currencyconverter.data.CurrencyApi
import com.app.currencyconverter.data.local.dao.CurrencyDao
import com.app.currencyconverter.data.models.CurrencyResponse
import com.app.currencyconverter.utils.Resource
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val dao: CurrencyDao,
    private val api: CurrencyApi
) : MainRepository {
    override suspend fun getRemoteCurrencyRates(): Resource<CurrencyResponse> {


        return try {
            val response = api.getRates()
            val result = response.body()
            if (response.isSuccessful && result != null) {

                // sync all remote data into a local database in a separate thread
                Thread {
                    dao.insertCurrencies(result)
                }.start()
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error!")
        }
    }

    override suspend fun getRatesLocally(): Resource<CurrencyResponse> {
        val localData = dao.getCurrencyData()

        return if (localData?.rates != null) {
            Resource.Success(localData)
        } else {
            Resource.Error("Something went wrong")
        }
    }

}