package com.app.currencyconverter.data

import com.app.currencyconverter.BuildConfig
import com.app.currencyconverter.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("latest.json")
    suspend fun getRates(
        @Query("app_id") appId:String = BuildConfig.API_KEY
    ): Response<CurrencyResponse>

}