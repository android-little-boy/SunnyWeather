package com.example.sunnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    private val placeService=ServiceCreator.create<PlaceService>()  //创建PlaceService接口的动态代理对象
    suspend fun searchPlaces(query:String)= placeService.searchPlaces(query).await() //新建挂起函数实现搜索城市
    private suspend fun <T> Call<T>.await():T{     //利用协程知识简化Retrofit的回调写法
        return suspendCoroutine { continuation ->
            enqueue(object:Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if (body!=null)continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}