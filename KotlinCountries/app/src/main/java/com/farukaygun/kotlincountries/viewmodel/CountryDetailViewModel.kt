package com.farukaygun.kotlincountries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farukaygun.kotlincountries.model.Country

class CountryDetailViewModel : ViewModel() {
    val countryLiveData = MutableLiveData<Country>()

    fun getDataFromRoom() {
        val country = Country("Turkey", "Ankara", "Avrupa", "TRY", "Türkçe", "www.ss.com")
        countryLiveData.value = country
    }
}