package eu.agribot.agribotapp.domain.service

import android.util.Log
import eu.agribot.agribotapp.communication.restapi.RestApiService
import eu.agribot.agribotapp.domain.model.todomain.waetherresponsemodels.WeatherResponseData
import java.text.SimpleDateFormat
import java.util.*

class WeatherService(private val restApiService: RestApiService) {

    fun getCurrentData() {
        restApiService.getCurrentData(onGotWeatherResponse = { logData(it.toDomainModel()) })
    }

    private fun logData(weatherResponseData: WeatherResponseData) {
        weatherResponseData.hourly?.forEach {
            Log.e(
                "xd",
                String.format(
                    "Date : %S | Temperature %S  | Humidity: %S )",
                    getDateTime(it.dt),
                    it.temp,
                    it.humidity
                )
            )

        }
        Log.e("xd", "received: " + weatherResponseData.hourly)
    }

    private fun getDateTime(s: Int): String? {
        Log.e("xd", "timestamp:: " + s)
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy | HH:mm")
            val netDate = Date(s * 1000L)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }


}