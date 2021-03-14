package eu.agribot.agribotapp.communication.restapi

import eu.agribot.agribotapp.communication.restapi.models.WeatherResponseModelApiData

interface RestApiService {

    fun getOpenWeatherDataFromApi(onGotWeatherResponse: (weatherResponse: WeatherResponseModelApiData) -> Unit)

    fun getOpenWeatherDataFromPersistent(): WeatherResponseModelApiData?
}
