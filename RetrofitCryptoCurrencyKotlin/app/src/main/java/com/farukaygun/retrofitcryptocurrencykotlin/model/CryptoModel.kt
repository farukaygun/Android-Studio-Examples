package com.farukaygun.retrofitcryptocurrencykotlin.model


data class CryptoModel(
    //@SerializedName("currency")
    val currency: String,
    //@SerializedName("price")
    val price: String
)