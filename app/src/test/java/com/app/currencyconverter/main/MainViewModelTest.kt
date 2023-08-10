@file:OptIn(ExperimentalCoroutinesApi::class)

package com.app.currencyconverter.main

import app.cash.turbine.test
import com.app.currencyconverter.data.models.CurrencyResponse
import com.app.currencyconverter.data.models.Rates
import com.app.currencyconverter.utils.CommonUtils
import com.app.repositories.FakeMainRepository
import com.app.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test


class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var fakeRepository: FakeMainRepository
    private val testDispatcherProvider = TestDispatcherProvider()

    @Before
    fun setup() {
        fakeRepository = FakeMainRepository()
        fakeRepository.setCurrencyResponse(CurrencyResponse(1, "USA", "", "", Rates(), 12093))
        fakeRepository.setNetworkErrorFlag(true)
        mainViewModel = MainViewModel(fakeRepository, testDispatcherProvider)
    }

    @After
    fun finish() {

    }

    @Test
    fun `set last and current timestamps and sync required will return the true value`() {
        assertThat(mainViewModel.isSycRequired(1, 18001)).isEqualTo(true)
    }


    @Test
    fun `set last and current timestamps and sync required will return the false value`() {
        assertThat(mainViewModel.isSycRequired(1800, 2000)).isEqualTo(false)
    }

    @Test
    fun `set valid rate of given currencies return corresponding rate`() {
        assertThat(mainViewModel.getRateForCurrency("USD", Rates())).isEqualTo(Rates().USD)
    }

    @Test
    fun `set in-valid rate of given currencies return null rate`() {
        assertThat(mainViewModel.getRateForCurrency("ABC", Rates())).isEqualTo(null)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `set amount zero and return error`() {
        runTest {
            val job = launch {
                mainViewModel.allConvertedCurrencies.test {
                    val emission = awaitItem() as MainViewModel.AllCurrencyEvents.Failure
                    assertThat(emission.errorText).isEqualTo(CommonUtils.INVALID_AMOUNT)
                    cancelAndIgnoreRemainingEvents()
                }
            }
            mainViewModel.allCurrenciesConverter(
                listOf("USD", "PKR"),
                "0",
                "USD"
            )

            job.join()
            job.cancel()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `set network disabled method will return error`() {
        runTest {
            val job = launch {
                mainViewModel.allConvertedCurrencies.test {
                    val emission = awaitItem() as MainViewModel.AllCurrencyEvents.Failure
                    assertThat(emission.errorText).isEqualTo(CommonUtils.NETWORK_ERROR)
                    cancelAndIgnoreRemainingEvents()
                }
            }
            mainViewModel.getRemoteCurrenciesRate("USD", 500F, listOf("USD", "PKR"))

            job.join()
            job.cancel()
        }
    }


    @Test
    fun `Test converted currencies list with valid value return a valid converted amount`(){
        val list = mainViewModel.getConvertedCurrenciesList(listOf("USD"),Rates(),100.0)

        assertThat(list[0]).isEqualTo("100.0 USD")
    }

    @Test
    fun `Test converted currencies list a currency not in the rates list`(){
        val list = mainViewModel.getConvertedCurrenciesList(listOf("ABC"),Rates(),100.0)

        assertThat(list.size).isEqualTo(0)
    }
}