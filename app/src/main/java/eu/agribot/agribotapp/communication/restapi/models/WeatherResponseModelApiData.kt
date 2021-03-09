package eu.agribot.agribotapp.communication.restapi.models

import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.communication.database.clouddatabase.model.BaseCloudDatabaseModel
import eu.agribot.agribotapp.domain.model.todomain.waetherresponsemodels.WeatherResponseData

data class WeatherResponseModelApiData(

    @SerializedName("lat")
    val lat: Float = 0.toFloat(),

    @SerializedName("lon")
    val lng: Float = 0.toFloat(),

    @SerializedName("timezone")
    val timezone: String? = null,

    @SerializedName("timezone_offset")
    val timezoneOffset: Int = 0,

    @SerializedName("hourly")
    val hourly: ArrayList<WeatherResponseHourlyItem>? = null

) : BaseCloudDatabaseModel<WeatherResponseData> {

    override fun toDomainModel() = WeatherResponseData(
        lat = this.lat,
        lng = this.lng,
        timezone = this.timezone,
        timezoneOffset = this.timezoneOffset,
        hourly = this.hourly?.map { it.toDomainModel() }
    )

}



