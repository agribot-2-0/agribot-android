package eu.agribot.agribotapp.ui.fragment.manualcontrol.recycler.diagnosticdatabar

import eu.agribot.agribotapp.base.BaseViewModel

data class DiagnosticDataViewModel(
    val diagnosticDataID: Int,
    val diagnosticDataValue: String,
    val diagnosticDataUnit: String
) : BaseViewModel()
