package com.example.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Place
import kotlin.concurrent.thread

class PlaceViewModel:ViewModel() {
    private val searchLiveData=MutableLiveData<String>()
    val placeList=ArrayList<Place>()
    val placeLiveData=Transformations.switchMap(searchLiveData){query-> //转换成可观测对象
        Repository.searchPlaces(query)  //搜索城市
    }
    fun searchPlaces(query:String){
        searchLiveData.value=query
    }
    fun savePlace(place:Place){
        thread {
            Repository.savePlace(place)
        }
    }
    val savedPlaceLiveData= MutableLiveData<Place>()
    fun getSavedPlace(){  //开启线程获取存储的地址
        thread {
            if(Repository.isPlaceSaved()){   //有存储的话才获取
                val place=Repository.getSavedPlace()
                savedPlaceLiveData.postValue(place)
            }
        }
    }
}