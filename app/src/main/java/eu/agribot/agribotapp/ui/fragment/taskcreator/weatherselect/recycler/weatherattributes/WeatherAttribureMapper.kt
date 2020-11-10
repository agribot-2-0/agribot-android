package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes

interface WeatherAttribureMapper<T> {
    fun toWeatherDataModel(): T
}
