package com.app.repositories

import com.app.currencyconverter.data.models.CurrencyResponse
import com.app.currencyconverter.main.MainRepository
import com.app.currencyconverter.utils.CommonUtils
import com.app.currencyconverter.utils.Resource

class FakeMainRepository : MainRepository {
    private lateinit var currencyResponse: CurrencyResponse

    private var shouldReturnNetworkError: Boolean = false

    fun setCurrencyResponse(currencyResponse: CurrencyResponse) {
        this.currencyResponse = currencyResponse
    }

    fun setNetworkErrorFlag(flag: Boolean) {
        shouldReturnNetworkError = flag
    }

    override suspend fun getRemoteCurrencyRates(): Resource<CurrencyResponse> {
        return if (shouldReturnNetworkError) {
            Resource.Error(CommonUtils.NETWORK_ERROR)
        } else
            Resource.Success(currencyResponse)
    }

    override suspend fun getRatesLocally(): Resource<CurrencyResponse> {

        return if (::currencyResponse.isInitialized ) {
            Resource.Success(currencyResponse)
        } else
            Resource.Error("Error")
    }
}