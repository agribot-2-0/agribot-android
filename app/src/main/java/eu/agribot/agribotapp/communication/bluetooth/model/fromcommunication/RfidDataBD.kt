package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

import eu.agribot.agribotapp.domain.model.todomain.RfidData

data class RfidDataBD(
    val antennaNumber: Int,
    val tagIDNumber: String
) : BaseBluetoothDataModel<RfidData> {
    override fun toDomainModel() = RfidData(
        antennaNumber = this.antennaNumber,
        tagIDNumber = this.tagIDNumber
    )
}
