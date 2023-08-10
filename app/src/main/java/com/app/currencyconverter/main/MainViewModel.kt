package com.app.currencyconverter.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.currencyconverter.data.models.CurrencyResponse
import com.app.currencyconverter.data.models.Rates
import com.app.currencyconverter.utils.CommonUtils
import com.app.currencyconverter.utils.DispatcherProvider
import com.app.currencyconverter.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository, private val dispatcher: DispatcherProvider
) : ViewModel() {

    sealed class AllCurrencyEvents {
        class Success(val allCurrencies: ArrayList<String>) : AllCurrencyEvents()
        class Failure(val errorText: String) : AllCurrencyEvents()
        object Loading : AllCurrencyEvents()
        object Empty : AllCurrencyEvents()
    }

    private val _allConvertedCurrencies =
        MutableStateFlow<AllCurrencyEvents>(AllCurrencyEvents.Empty)
    val allConvertedCurrencies: StateFlow<AllCurrencyEvents> = _allConvertedCurrencies


    /**
     * @param currencyNames are the drop down list name of all selectable currencies
     * @param amountStr is user entered amount
     * @param fromCurrency user selected currency
     */
    fun allCurrenciesConverter(
        currencyNames: List<String>,
        amountStr: String,
        fromCurrency: String
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null || fromAmount <= 0) {
            _allConvertedCurrencies.value = AllCurrencyEvents.Failure(CommonUtils.INVALID_AMOUNT)
            return
        }

        viewModelScope.launch(dispatcher.io) {
            _allConvertedCurrencies.value = AllCurrencyEvents.Loading

            // First check if local data exist or not
            when (val ratesResponse = repository.getRatesLocally()) {
                is Resource.Success -> {
                    val lastSyncTimestamp = ratesResponse.data!!.timestamp

                    // If local data exist then check if sync from remote required or not and post data accordingly
                    if (isSycRequired(lastSyncTimestamp, CommonUtils.getCurrentUTCTimeStamp())) {
                        getRemoteCurrenciesRate(fromCurrency, fromAmount, currencyNames)
                    } else {
                        postResponse(ratesResponse, fromCurrency, fromAmount, currencyNames)
                    }
                }

                is Resource.Error -> {
                    // In case of failure or no data in local exist then fetch from remote.
                    getRemoteCurrenciesRate(fromCurrency, fromAmount, currencyNames)
                }
            }
        }
    }

    /**
     * @param currency code which needs to be searched from rates list
     * @param rates all the currency rates list with reference to one currency
     * @return a rate of selected currency
     */
    fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "AED" -> rates.AED
        "AFN" -> rates.AFN
        "ALL" -> rates.ALL
        "AMD" -> rates.AMD
        "ANG" -> rates.ANG
        "AOA" -> rates.AOA
        "ARS" -> rates.ARS
        "AUD" -> rates.AUD
        "AWG" -> rates.AWG
        "AZN" -> rates.AZN
        "BAM" -> rates.BAM
        "BBD" -> rates.BBD
        "BDT" -> rates.BDT
        "BGN" -> rates.BND
        "BHD" -> rates.BHD
        "BIF" -> rates.BIF
        "BMD" -> rates.BMD
        "BND" -> rates.BND
        "BOB" -> rates.BOB
        "BRL" -> rates.BRL
        "BSD" -> rates.BSD
        "BTC" -> rates.BTC
        "BTN" -> rates.BTN
        "BWP" -> rates.BWP
        "BYN" -> rates.BYN
        "BZD" -> rates.BZD
        "CAD" -> rates.CAD
        "CDF" -> rates.CDF
        "CHF" -> rates.CHF
        "CLF" -> rates.CLF
        "CLP" -> rates.CLP
        "CNH" -> rates.CNH
        "CNY" -> rates.CNY
        "COP" -> rates.COP
        "CRC" -> rates.CRC
        "CUC" -> rates.CUC
        "CUP" -> rates.CUP
        "CVE" -> rates.CVE
        "CZK" -> rates.CZK
        "DJF" -> rates.DJF
        "DKK" -> rates.DKK
        "DOP" -> rates.DOP
        "DZD" -> rates.DZD
        "EGP" -> rates.EGP
        "ERN" -> rates.ERN
        "ETB" -> rates.ETB
        "EUR" -> rates.EUR
        "FJD" -> rates.FJD
        "FKP" -> rates.FKP
        "GBP" -> rates.GBP
        "GEL" -> rates.GEL
        "GGP" -> rates.GGP
        "GHS" -> rates.GHS
        "GIP" -> rates.GIP
        "GMD" -> rates.GMD
        "GNF" -> rates.GNF
        "GTQ" -> rates.GTQ
        "GYD" -> rates.GYD
        "HKD" -> rates.HKD
        "HNL" -> rates.HNL
        "HRK" -> rates.HRK
        "HTG" -> rates.HTG
        "HUF" -> rates.HUF
        "IDR" -> rates.IDR
        "ILS" -> rates.ILS
        "IMP" -> rates.IMP
        "INR" -> rates.INR
        "IQD" -> rates.IQD
        "IRR" -> rates.IRR
        "ISK" -> rates.ISK
        "JEP" -> rates.JEP
        "JMD" -> rates.JMD
        "JOD" -> rates.JOD
        "JPY" -> rates.JPY
        "KES" -> rates.KES
        "KGS" -> rates.KGS
        "KHR" -> rates.KHR
        "KMF" -> rates.KMF
        "KPW" -> rates.KPW
        "KRW" -> rates.KRW
        "KWD" -> rates.KWD
        "KYD" -> rates.KYD
        "KZT" -> rates.KZT
        "LAK" -> rates.LAK
        "LBP" -> rates.LBP
        "LKR" -> rates.LKR
        "LRD" -> rates.LRD
        "LSL" -> rates.LSL
        "LYD" -> rates.LYD
        "MAD" -> rates.MAD
        "MDL" -> rates.MDL
        "MGA" -> rates.MGA
        "MKD" -> rates.MKD
        "MMK" -> rates.MMK
        "MNT" -> rates.MNT
        "MOP" -> rates.MOP
        "MRU" -> rates.MRU
        "MUR" -> rates.MUR
        "MVR" -> rates.MVR
        "MWK" -> rates.MWK
        "MXN" -> rates.MXN
        "MYR" -> rates.MYR
        "MZN" -> rates.MZN
        "NAD" -> rates.NAD
        "NGN" -> rates.NGN
        "NIO" -> rates.NIO
        "NOK" -> rates.NOK
        "NPR" -> rates.NPR
        "NZD" -> rates.NZD
        "OMR" -> rates.OMR
        "PAB" -> rates.PAB
        "PEN" -> rates.PEN
        "PGK" -> rates.PGK
        "PHP" -> rates.PHP
        "PKR" -> rates.PKR
        "USD" -> rates.USD
        else -> null
    }

    /**
     * Calculate the conversion of all the currencies with reference to selected currency
     */
     fun getConvertedCurrenciesList(
        currencyNames: List<String>,
        rates: Rates,
        fromDollarRate: Double
    ): ArrayList<String> {
        val output = ArrayList<String>()

        currencyNames.forEach {
            val rate = getRateForCurrency(it, rates)

            if (rate != null) {
                output.add("${((rate * fromDollarRate) * 100.0).roundToInt() / 100.0} $it")
            }
        }
        return output

    }

    /**
     * @param lastSyncTimestamp saved timestamp of last sync from remote data
     * @param currentTimestamp current timestamp to compare
     * @return A boolean value if sync with remote is required or not
     */
    fun isSycRequired(lastSyncTimestamp: Int, currentTimestamp: Long): Boolean {
        return (currentTimestamp >= lastSyncTimestamp + CommonUtils.SECONDS_TO_ADD)
    }

    /**
     * Request to fetch data from remote
     */
    suspend fun getRemoteCurrenciesRate(
        fromCurrency: String,
        fromAmount: Float,
        currencyNames: List<String>
    ) {
        when (val ratesResponse = repository.getRemoteCurrencyRates()) {
            is Resource.Error -> _allConvertedCurrencies.value =
                AllCurrencyEvents.Failure(ratesResponse.message!!)

            is Resource.Success -> {
                postResponse(ratesResponse, fromCurrency, fromAmount, currencyNames)
            }
        }
    }

    /**
     * post data in Kotlin flow to reflect on UI
     */
    private fun postResponse(
        ratesResponse: Resource<CurrencyResponse>,
        fromCurrency: String,
        fromAmount: Float,
        currencyNames: List<String>
    ) {
        val rates = ratesResponse.data!!.rates
        val rate = getRateForCurrency(fromCurrency, rates!!)

        if (rate == null) {
            _allConvertedCurrencies.value = AllCurrencyEvents.Failure("Rate not found")
        } else {
            val amountInDollars = fromAmount / rate
            val list = getConvertedCurrenciesList(currencyNames, rates, amountInDollars)

            _allConvertedCurrencies.value = AllCurrencyEvents.Success(list)
        }
    }
}
