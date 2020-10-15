package eu.agribot.agribotapp.domain.service

import eu.agribot.agribotapp.domain.model.program.weather.toCommunication.NewRobotTaskDetails

class AutonomyService(private val bluetoothConnectionService: BluetoothConnectionService) {

    fun sendInitAutonomyMode() {
        bluetoothConnectionService.initAutonomyMode()
    }

    fun programTaskToRobot(taskName: String, newRobotTaskDetails: NewRobotTaskDetails) {
      //  cloudDatabase.createNewTask(taskName, newRobotTaskDetails.toCommunicationModel())
    }
}
