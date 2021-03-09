package eu.agribot.agribotapp.communication.restapi

import eu.agribot.agribotapp.communication.restapi.models.WeatherResponseModelApiData

interface RestApiService {

    fun getCurrentData(onGotWeatherResponse: (weatherResponse: WeatherResponseModelApiData) -> Unit)
}