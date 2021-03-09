package eu.agribot.agribotapp.communication.restapi.models

import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.communication.database.clouddatabase.model.BaseCloudDatabaseModel
import eu.agribot.agribotapp.domain.model.todomain.waetherresponsemodels.WeatherItem

data class WeatherResponseWeatherItem(

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("main")
    val main: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("icon")
    val icon: String? = null

) : BaseCloudDatabaseModel<WeatherItem> {

    override fun toDomainModel() = WeatherItem(
        id = this.id,
        main = this.main,
        description = this.description,
        icon = this.icon
    )
}
