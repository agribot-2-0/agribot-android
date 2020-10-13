package eu.agribot.agribotapp.communication.database.clouddatabase

object CloudDatabasePaths {
    const val COLLECTION_ENGINE = "engine"
    const val DOCUMENT_HEALTH = "health"

    const val COLLECTION_ROBOTS = "robots"
    const val DOCUMENT_ADI75R1_HEALTH_ENGINE = "ADI75R1/health/engine"
    const val DOCUMENT_ADI75R1_LOCALIZATION_ORIENTATION = "ADI75R1/localization/orientation"
    const val DOCUMENT_ADI75R1_LOCALIZATION_POSE = "ADI75R1/localization/pose"
    const val DOCUMENT_ADI75R1_WEATHER_STATION_HUMIDITYE = "ADI75R1/weather_station/humidity"
    const val DOCUMENT_ADI75R1_WEATHER_STATION_TEMPERATURE = "ADI75R1/weather_station/temperature"
    const val DOCUMENT_ADI75R1_WEATHER_STATION_WIND = "ADI75R1/weather_station/wind"
    const val DOCUMENT_ADI75R1_WEATHER_STATION_PRESSURE = "ADI75R1/weather_station/pressure"

    const val DOCUMENT_ADI75R1_ADI75R1_TASKS = "ADI75R1/tasks"
    const val DOCUMENT_ADI75R1_TASKS_LIST = "ADI75R1/tasks/"

    const val DOCUMENT_ADI75R2_LOCALIZATION_POSE = "ADI75R2/localization/pose"

    const val COLLECTION_USER = "users"
    const val DOCUMENT_ROBOTS_ADI75R1 = "/robots/ADI75R1"
    const val COLLECTION_TASK_ITEM = "robots/ADI75R1/tasks"
}
