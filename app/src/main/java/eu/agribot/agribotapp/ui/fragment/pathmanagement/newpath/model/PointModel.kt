package eu.agribot.agribotapp.ui.fragment.pathmanagement.newpath.model

import eu.agribot.agribotapp.domain.model.CoordinatesModel

data class PointModel(
    val pointId: Int,
    val coordinates: CoordinatesModel
)