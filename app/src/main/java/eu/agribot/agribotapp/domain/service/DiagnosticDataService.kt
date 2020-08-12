package eu.agribot.agribotapp.domain.service

import androidx.lifecycle.MutableLiveData
/**
 * Service used to handle status info from Agribot
 */
class DiagnosticDataService {
    val engineRotationsPerMinute: MutableLiveData<String> = MutableLiveData()
    val torqueOfEngine: MutableLiveData<String> = MutableLiveData()
    val engineFuelRate: MutableLiveData<String> = MutableLiveData()
    val fuelLevel: MutableLiveData<String> = MutableLiveData()
    val fuelTemperature: MutableLiveData<String> = MutableLiveData()
    val batteryVoltage: MutableLiveData<String> = MutableLiveData()

    fun readFirstEngineData(
        engineRotationsPerMinute: Int,
        torqueOfEngine: Int,
        engineFuelRate: Int,
        fuelLevel: Int,
        fuelTemperature: Int,
        batteryVoltage: Double
    ) {
        val batteryInfo: String = if (batteryVoltage.toString().length > 4) {
            batteryVoltage.toString().slice(0..4)
        } else {
            batteryVoltage.toString()
        }

        val fuel: Double = engineFuelRate * 0.1

        this.engineRotationsPerMinute.postValue(engineRotationsPerMinute.toString())
        this.torqueOfEngine.postValue(torqueOfEngine.toString())
        this.engineFuelRate.postValue(fuel.toString())
        this.fuelLevel.postValue(fuelLevel.toString())
        this.fuelTemperature.postValue(fuelTemperature.toString())
        this.batteryVoltage.postValue(batteryInfo)
    }
}
