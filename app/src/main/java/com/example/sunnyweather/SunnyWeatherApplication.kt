package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application(){
    companion object{
        lateinit var context:Context //书中说会报错需要加一个注解@SuppressLint("StaticFieldLeak")，这里没有报错，暂时不加先
        const val TOKEN="令牌"//暂时还没有审核通过，先以“令牌”代替
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}