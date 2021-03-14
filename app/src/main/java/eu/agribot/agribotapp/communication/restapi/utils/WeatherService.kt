package eu.agribot.agribotapp.communication.restapi.utils

import eu.agribot.agribotapp.communication.restapi.models.WeatherResponseModelApiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/onecall?")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("APPID") app_id: String,
        @Query("units") units: String
    ): Call<WeatherResponseModelApiData>
}
