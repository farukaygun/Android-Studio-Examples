package com.farukaygun.kotlincountries.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.farukaygun.kotlincountries.databinding.CountriesRecyclerviewRowBinding
import com.farukaygun.kotlincountries.model.Country
import com.farukaygun.kotlincountries.util.downloadFromURL
import com.farukaygun.kotlincountries.util.placeHolderProgressBar
import com.farukaygun.kotlincountries.view.FeedFragmentDirections

class recyclerViewCountriesAdapter(val countryList: ArrayList<Country>) : RecyclerView.Adapter<recyclerViewCountriesAdapter.CountryViewHolder>() {
    class CountryViewHolder(var binding: CountriesRecyclerviewRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = CountriesRecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.binding.textViewCountryName.text = countryList[position].countryName
        holder.binding.textViewCountryRegion.text = countryList[position].countryRegion

        holder.binding.root.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryDetailFragment(0)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.imageViewCountryFlag.downloadFromURL(countryList[position].countryFlagUrl, placeHolderProgressBar(holder.itemView.context))
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateCountryList(newCountryList: List<Country>) {
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }
}