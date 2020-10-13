package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationWind

class FirstRobotWeatherStationWindCDB(val speed: Int = 1) :
    BaseCloudDatabaseModel<FirstRobotWeatherStationWind> {
    override fun toDomainModel() = FirstRobotWeatherStationWind(
        speed = this.speed
    )
}
