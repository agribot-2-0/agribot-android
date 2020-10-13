package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.FirstRobotWeatherStationPressure

class FirstRobotWeatherStationPressureCDB(val pressure: Int = -1) :
    BaseCloudDatabaseModel<FirstRobotWeatherStationPressure> {
    override fun toDomainModel() = FirstRobotWeatherStationPressure(
        pressure = this.pressure
    )
}
