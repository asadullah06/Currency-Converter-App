package com.app.utils

import com.app.currencyconverter.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
class TestDispatcherProvider : DispatcherProvider {
    val testDispatcher = StandardTestDispatcher()

    init {
        Dispatchers.setMain(testDispatcher)
    }

    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher
    override val unconfined: CoroutineDispatcher
        get() = testDispatcher


}