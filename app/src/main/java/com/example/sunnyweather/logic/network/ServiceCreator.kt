package com.example.sunnyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  ServiceCreator {
    private const val BASE_URL="https://api.caiyunapp.com/"//retrofit 请求的根地址，与PlaceService中的请求地址拼接
    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass:Class<T>):T= retrofit.create(serviceClass)
    inline fun <reified T> create():T= create(T::class.java)


}