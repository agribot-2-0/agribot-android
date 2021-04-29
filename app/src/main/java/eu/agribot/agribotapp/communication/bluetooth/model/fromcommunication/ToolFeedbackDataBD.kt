package eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication

import eu.agribot.agribotapp.domain.model.todomain.ToolFeedbackData

data class ToolFeedbackDataBD(
    val toolID: Int,
    val toolValue: Int
) : BaseBluetoothDataModel<ToolFeedbackData> {
    override fun toDomainModel() = ToolFeedbackData(
        toolId = this.toolID,
        toolValue = this.toolValue
    )
}
