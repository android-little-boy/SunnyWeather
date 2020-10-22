package com.example.sunnyweather.ui.place

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*
import java.math.BigDecimal

class PlaceAdapter(private val fragment:PlaceFragment,private val placeList:List<Place>) :
        RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val placeName:TextView=view.findViewById(R.id.placeName)
        val placeAddress:TextView=view.findViewById(R.id.placeAddress)
        val location:TextView=view.findViewById(R.id.location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder=ViewHolder(view)
        holder.itemView.setOnClickListener{
            val position=holder.adapterPosition
            val place=placeList[position]
            val activity=fragment.activity
            Log.d("mmmm", "activity is:${activity} ")
            if (activity is WeatherActivity){
                activity.drawerLayout.closeDrawers()
                activity.viewModel.locationLng=place.location.lng
                activity.viewModel.locationLat=place.location.lat
                activity.viewModel.placeName=place.name
                activity.refreshWeather()
            }else {
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
                fragment.activity?.finish()
            }
            fragment.viewModel.savePlace(place)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place=placeList[position]
        holder.placeName.text=place.name
        holder.placeAddress.text=place.address
        val lng=place.location.lng.toDouble()
        val lat=place.location.lat.toDouble()
        val bg=BigDecimal(lng)
        val bg1=BigDecimal(lat)
        val locationLng=bg.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
        val locationLat=bg1.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
        val locationText="经度：${locationLng}  纬度：${locationLat}"
        holder.location.text=locationText
        holder.placeAddress.setSelected(true)

    }

    override fun getItemCount()=placeList.size    //kotlin简化只有一行代码的返回
}