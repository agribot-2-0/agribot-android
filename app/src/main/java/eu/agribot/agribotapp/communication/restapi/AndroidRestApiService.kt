package eu.agribot.agribotapp.communication.restapi

import android.util.Log
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.communication.restapi.models.WeatherResponseModelApiData
import eu.agribot.agribotapp.communication.restapi.utils.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AndroidRestApiService(
    val resourcesProvider: ResourcesProvider
) : RestApiService {


    override fun getCurrentData(onGotWeatherResponse: (weatherResponse: WeatherResponseModelApiData) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl(resourcesProvider.getString(R.string.open_weather_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(
            lat,
            lon,
            exclude,
            resourcesProvider.getString(R.string.open_weather_api_key),
            units
        )
        call.enqueue(object : Callback<WeatherResponseModelApiData> {
            override fun onResponse(
                call: Call<WeatherResponseModelApiData>,
                responseModel: Response<WeatherResponseModelApiData>
            ) {
                responseModel.body()?.let { onGotWeatherResponse(it) }
            }

            override fun onFailure(call: Call<WeatherResponseModelApiData>, t: Throwable) {
                Log.e("xd", t.message)
            }
        })
    }

    companion object {
        var lat = "50.47124576025691"
        var lon = "17.307626876732318"
        var exclude = "daily"
        var units = "metric"
    }
}