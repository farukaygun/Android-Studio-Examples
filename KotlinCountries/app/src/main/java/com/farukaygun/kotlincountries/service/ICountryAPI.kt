package com.farukaygun.kotlincountries.service

import com.farukaygun.kotlincountries.model.Country
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ICountryAPI {
    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    fun getCountries() : Single<List<Country>>
}