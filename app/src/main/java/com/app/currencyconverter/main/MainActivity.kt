package com.app.currencyconverter.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.app.currencyconverter.R
import com.app.currencyconverter.databinding.ActivityMainBinding
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var convertedCurrenciesAdapter: ConvertedCurrenciesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etAmount.doAfterTextChanged {

        }

        binding.etAmount.textChanges().debounce(1, TimeUnit.SECONDS).subscribe {
            if (it.toString().isNotEmpty()) {
                fetchConvertedAmounts()
            }
        }
        binding.spFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (binding.etAmount.text.toString().isNotEmpty()) {
                    fetchConvertedAmounts()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        lifecycleScope.launchWhenStarted {
            viewModel.allConvertedCurrencies.collect {
                when (it) {
                    is MainViewModel.AllCurrencyEvents.Success -> {
                        binding.progressBar.isVisible = false

                        if (::convertedCurrenciesAdapter.isInitialized.not())
                            setAdapter(it.allCurrencies)
                        else {
                            convertedCurrenciesAdapter.currenciesList.clear()
                            convertedCurrenciesAdapter.currenciesList.addAll(it.allCurrencies)
                            convertedCurrenciesAdapter.notifyDataSetChanged()
                        }
                    }

                    is MainViewModel.AllCurrencyEvents.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is MainViewModel.AllCurrencyEvents.Failure -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(this@MainActivity, it.errorText, Toast.LENGTH_LONG).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun fetchConvertedAmounts() {
        val currenciesList = resources.getStringArray(R.array.currency_codes).toList()
        viewModel.allCurrenciesConverter(
            currenciesList,
            binding.etAmount.text.toString(),
            binding.spFrom.selectedItem.toString()
        )
    }

    private fun setAdapter(list: ArrayList<String>) {
        convertedCurrenciesAdapter = ConvertedCurrenciesAdapter(list)
        val layoutManager = GridLayoutManager(this, 3)
        binding.currenciesList.layoutManager = layoutManager
        binding.currenciesList.adapter = convertedCurrenciesAdapter
    }
}