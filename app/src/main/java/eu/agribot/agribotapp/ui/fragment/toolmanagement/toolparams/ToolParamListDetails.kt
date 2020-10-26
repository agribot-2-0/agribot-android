package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparams

import eu.agribot.agribotapp.domain.model.cdb.ToolParamDetails

data class ToolParamListDetails(
        val toolName: String,
        val toolParametrDetails: ToolParamDetails
)