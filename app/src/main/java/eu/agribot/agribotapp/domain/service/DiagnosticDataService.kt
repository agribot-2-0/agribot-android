package eu.agribot.agribotapp.domain.service

import androidx.lifecycle.MutableLiveData
/**
 * Service used to handle status info from Agribot
 */
class DiagnosticDataService {
    val engineRotationsPerMinute: MutableLiveData<Int> = MutableLiveData()
    val torqueOfEngine: MutableLiveData<Int> = MutableLiveData()
    val engineFuelRate: MutableLiveData<Double> = MutableLiveData()
    val fuelLevel: MutableLiveData<Int> = MutableLiveData()
    val fuelTemperature: MutableLiveData<Int> = MutableLiveData()
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

        this.engineRotationsPerMinute.postValue(engineRotationsPerMinute)
        this.torqueOfEngine.postValue(torqueOfEngine)
        this.engineFuelRate.postValue(fuel)
        this.fuelLevel.postValue(fuelLevel)
        this.fuelTemperature.postValue(fuelTemperature)
        this.batteryVoltage.postValue(batteryInfo)
    }
}
