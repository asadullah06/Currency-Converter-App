package com.app.currencyconverter.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.currencyconverter.R

class ConvertedCurrenciesAdapter(var currenciesList: ArrayList<String>) :
    RecyclerView.Adapter<ConvertedCurrenciesAdapter.ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return currenciesList.size
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.amountTextview.text = currenciesList[position]
    }

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val amountTextview: TextView = itemView.findViewById(R.id.tv_converted_amount)
    }
}