package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.communication.restapi.RestApiService

class WeatherService(private val restApiService: RestApiService){

    fun getCurrentData(){
        restApiService.getCurrentData()
    }
}