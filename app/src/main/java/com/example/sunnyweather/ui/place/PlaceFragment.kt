package com.example.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.MainActivity
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.network.WeatherService
import com.example.sunnyweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {
    val viewModel by lazy {
        ViewModelProvider(this)
                .get(PlaceViewModel::class.java)
    }  //懒加载技术，使用时赋值，并且可缓存
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_place, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /***
         * 结合liveData利用开启线程去获取存储的地址
         * 虽然在开启应用的时候没什么必要，但学的东西自己试一下还是感觉很好的
         */
        if (activity is MainActivity) {
            viewModel.getSavedPlace()
        }
        viewModel.savedPlaceLiveData.observe(viewLifecycleOwner, Observer { place ->
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
        })
        /*if (activity is MainActivity && Repository.isPlaceSaved()){
            val place=Repository.getSavedPlace()
            val intent=Intent(context,WeatherActivity::class.java).apply {
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("place_name",place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }*/
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager       //直接引用id获取控件需要加kotlin-android-extensions插件
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter
        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull() //获取Resul封装的value值，获取查询到的地址
            if (places != null) {
                recyclerView.visibility = View.VISIBLE //recyclerview设为可见
                bgImageView.visibility = View.GONE  //背景设为不可见
                viewModel.placeList.clear()//清空缓存数据，保证数据唯一性
                viewModel.placeList.addAll(places)//查询到的数据设置给viewModel，传递给adapter
                adapter.notifyDataSetChanged()//刷新数据
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}