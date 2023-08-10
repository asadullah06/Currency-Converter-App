package com.app.currencyconverter.utils

import java.util.Calendar
import java.util.TimeZone

object CommonUtils {

     fun getCurrentUTCTimeStamp():Long{
        val c = Calendar.getInstance()
        c.timeZone = TimeZone.getTimeZone("UTC")

        return c.timeInMillis / 1000
    }

    const val SECONDS_TO_ADD = 1800
    const val INVALID_AMOUNT = "Invalid amount"
    const val NETWORK_ERROR = "Network error"
    const val BASE_URL = "https://openexchangerates.org/api/"
}