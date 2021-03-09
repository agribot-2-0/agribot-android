package eu.agribot.agribotapp.communication.restapi

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AndroidRestApiService : RestApiService {


    override fun getCurrentData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, exclude, AppId)
        call.enqueue(object : Callback<WeatherResponseModelApiData> {
            override fun onResponse(
                call: Call<WeatherResponseModelApiData>,
                responseModel: Response<WeatherResponseModelApiData>
            ) {
                if (responseModel.code() == 200) {
                    val weatherResponse = responseModel.body()

                    Log.e("xd", "resp: " + (responseModel.body()))
                }

            }

            override fun onFailure(call: Call<WeatherResponseModelApiData>, t: Throwable) {
                Log.e("xd", t.message)
            }
        })
    }

    companion object {

        var BaseUrl = "http://api.openweathermap.org/"
        var AppId = "954a47b02cc8bce6a6e1e6976a264244"
        var lat = "50.47124576025691"
        var lon = "17.307626876732318"
        var exclude = "daily"
    }
}