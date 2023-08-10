package com.app.currencyconverter.di

import android.app.Application
import androidx.room.Room
import com.app.currencyconverter.data.CurrencyApi
import com.app.currencyconverter.data.local.CurrenciesDatabase
import com.app.currencyconverter.main.DefaultMainRepository
import com.app.currencyconverter.main.MainRepository
import com.app.currencyconverter.utils.CommonUtils
import com.app.currencyconverter.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Tag
import javax.inject.Singleton



@Module
@InstallIn(ApplicationComponent::class)
object AppModel {


    @Singleton
    @Provides
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(CommonUtils.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyApi,currenciesDatabase: CurrenciesDatabase): MainRepository = DefaultMainRepository(currenciesDatabase.currencyDao(),api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }

    @Singleton
    @Provides
    fun provideCurrencyDatabase(application: Application): CurrenciesDatabase {
        return Room.databaseBuilder(
            application, CurrenciesDatabase::class.java,
            CurrenciesDatabase.DATABASE_NAME
        ).build()
    }
}