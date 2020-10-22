package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application(){
    companion object{
        lateinit var context:Context //书中说会报错需要加一个注解@SuppressLint("StaticFieldLeak")，这里没有报错，暂时不加先
        const val TOKEN="rG0bJBAg3YtIdtJ6"//彩云天气api接口令牌
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}