package eu.agribot.agribotapp.communication.restapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApiService {

    fun getCurrentData()
}