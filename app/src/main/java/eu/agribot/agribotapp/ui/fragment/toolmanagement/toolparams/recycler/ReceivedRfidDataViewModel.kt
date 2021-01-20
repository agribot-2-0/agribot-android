package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams.recycler

import eu.agribot.agribotapp.base.BaseViewModel

data class ReceivedRfidDataViewModel(
    val antennaNumber: String,
    val rfidTagData: String,
    val rfidTagDataArray: ByteArray

) : BaseViewModel()
