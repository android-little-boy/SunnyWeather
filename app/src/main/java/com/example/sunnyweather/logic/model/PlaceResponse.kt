package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName
import java.net.Inet4Address

data class PlaceResponse(val status:String,val places:List<Place>)
data class Place(val name:String,val lacation:Location,@SerializedName("formatted_address")val address:String)
data class Location(val lng:String,val lat:String)