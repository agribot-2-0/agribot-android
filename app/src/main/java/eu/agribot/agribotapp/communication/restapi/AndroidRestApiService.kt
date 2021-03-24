package eu.agribot.agribotapp.communication.restapi

import com.google.gson.Gson
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.communication.persistence.PersistentStorage
import eu.agribot.agribotapp.communication.restapi.models.WeatherResponseModelApiData
import eu.agribot.agribotapp.communication.restapi.utils.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class AndroidRestApiService(
    val resourcesProvider: ResourcesProvider,
    val persistentStorage: PersistentStorage
) : RestApiService {

    override fun getOpenWeatherDataFromApi(onGotWeatherResponse: (weatherResponse: WeatherResponseModelApiData) -> Unit) {
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
                responseModel.body()?.let {
                    persistentStorage.saveWeatherForecast(Gson().toJson(it))
                    onGotWeatherResponse(it)
                }
            }

            override fun onFailure(call: Call<WeatherResponseModelApiData>, t: Throwable) {
                Timber.e(
                    String.format(
                        resourcesProvider.getString(R.string.retrofit_exception),
                        t.message.toString()
                    )
                )
            }
        })
    }

    override fun getOpenWeatherDataFromPersistent(): WeatherResponseModelApiData? {
        return if (persistentStorage.getWeatherForecast() != "")
            Gson().fromJson(
                persistentStorage.getWeatherForecast(), WeatherResponseModelApiData::class.java
            )
        else null
    }

    companion object {
        var lat = "50.47124576025691"
        var lon = "17.307626876732318"
        var exclude = "daily"
        var units = "metric"
    }
}
