package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePath.TOOL_PHOTO_FILE_TYPE
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.HitchListItemDetail
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.ToolRecylerItemId
import kotlin.math.PI

class ToolDataManager(private val resourcesProvider: ResourcesProvider) {
    private val initString = ""
    private val initDouble = 0.0
    val noRfidTag = listOf<Int>()
    var toolName: String = initString
    var rfid: List<Int> = noRfidTag
    var pto: Double = initDouble
    var tplUpDown: Double = initDouble
    var tplPushPull: Double = initDouble
    var green: Double = initDouble
    var yellow: Double = initDouble
    var blue: Double = initDouble
    var vMax: Double = initDouble

    fun updateAllToolParameters(toolDetails: ToolDetails) {
        rfid = toolDetails.rfid
        pto = toolDetails.pto
        tplUpDown = toolDetails.tplUpDown
        tplPushPull = toolDetails.tplPushPull
        green = toolDetails.green
        blue = toolDetails.blue
        yellow = toolDetails.yellow
        vMax = toolDetails.vMax
    }

    fun getToolPhotoPathName(): String {
        return "/$toolName$TOOL_PHOTO_FILE_TYPE"
    }

    fun setSliderValue(sliderNumber: Int, sliderValue: Int) {
        when (sliderNumber) {
            ToolRecylerItemId.PTO.ID -> {
                pto = sliderValue.toDouble()
            }
            ToolRecylerItemId.TPL_UP_DOWN.ID -> {
                tplUpDown = sliderValue.toDouble()
            }
            ToolRecylerItemId.TPL_PUSH_PULL.ID -> {
                tplPushPull = sliderValue.toDouble()
            }
            ToolRecylerItemId.GREEN.ID -> {
                green = sliderValue.toDouble()
            }
            ToolRecylerItemId.YELLOW.ID -> {
                yellow = sliderValue.toDouble()
            }
            ToolRecylerItemId.BLUE.ID -> {
                blue = sliderValue.toDouble()
            }
            ToolRecylerItemId.V_MAX.ID -> {
                vMax = sliderValue.toDouble()
            }
        }
    }

    fun getMaximumRobotVelocity(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.V_MAX.ID,
            toolName = resourcesProvider.getString(R.string.v_max),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = convertDatabaseValueToSliderValue(vMax, Value.VELOCITY),
                toolUnit = resourcesProvider.getString(R.string.kilometer_per_hour),
                minValue = convertFinalValueToWorkingValue(MIN_VELOCITY, Value.VELOCITY),
                maxValue = convertFinalValueToWorkingValue(MAX_VELOCITY, Value.VELOCITY)
            )

        )
    }

    fun getRobotGreenFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.GREEN.ID,
            toolName = resourcesProvider.getString(R.string.green_flow),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = convertDatabaseValueToSliderValue(green, Value.COLOUR),
                toolUnit = resourcesProvider.getString(R.string.percent),
                minValue = convertFinalValueToWorkingValue(MIN_COLOUR, Value.COLOUR),
                maxValue = convertFinalValueToWorkingValue(MAX_COLOUR, Value.COLOUR)
            )
        )
    }

    fun getRobotYellowFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.YELLOW.ID,
            toolName = resourcesProvider.getString(R.string.yellow_flow),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = convertDatabaseValueToSliderValue(yellow, Value.COLOUR),
                toolUnit = resourcesProvider.getString(R.string.percent),
                minValue = convertFinalValueToWorkingValue(MIN_COLOUR, Value.COLOUR),
                maxValue = convertFinalValueToWorkingValue(MAX_COLOUR, Value.COLOUR)
            )
        )
    }

    fun getRobotBlueFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.BLUE.ID,
            toolName = resourcesProvider.getString(R.string.blue_flow),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = convertDatabaseValueToSliderValue(blue, Value.COLOUR),
                toolUnit = resourcesProvider.getString(R.string.percent),
                minValue = convertFinalValueToWorkingValue(MIN_COLOUR, Value.COLOUR),
                maxValue = convertFinalValueToWorkingValue(MAX_COLOUR, Value.COLOUR)
            )
        )
    }

    fun getRobotPtoFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.PTO.ID,
            toolName = resourcesProvider.getString(R.string.pto),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = convertDatabaseValueToSliderValue(pto, Value.PTO),
                toolUnit = resourcesProvider.getString(R.string.rpm),
                minValue = convertFinalValueToWorkingValue(MIN_PTO, Value.PTO),
                maxValue = convertFinalValueToWorkingValue(MAX_PTO, Value.PTO)
            )
        )
    }

    fun getRobotTplPushPullFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.TPL_PUSH_PULL.ID,
            toolName = resourcesProvider.getString(R.string.tpl_push_pull),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = convertDatabaseValueToSliderValue(
                    tplPushPull,
                    Value.TPL_PUSH_PULL
                ),
                toolUnit = resourcesProvider.getString(R.string.meters),
                minValue = convertFinalValueToWorkingValue(MIN_TPL_PUSH_PULL, Value.TPL_PUSH_PULL),
                maxValue = convertFinalValueToWorkingValue(MAX_TPL_PUSH_PULL, Value.TPL_PUSH_PULL)
            )
        )
    }

    fun getRobotTplUpDownFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.TPL_UP_DOWN.ID,
            toolName = resourcesProvider.getString(R.string.tpl_up_down),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = convertDatabaseValueToSliderValue(tplUpDown, Value.TPL_UP_DOWN),
                toolUnit = "\u00B0",
                minValue = convertFinalValueToWorkingValue(MIN_TPL_UP_DOWN, Value.TPL_UP_DOWN),
                maxValue = convertFinalValueToWorkingValue(MAX_TPL_UP_DOWN, Value.TPL_UP_DOWN)
            )
        )
    }

    fun convertFinalValueToWorkingValue(finalValue: Double, value: Value): Int {
        var workingValue = 0
        when (value) {
            Value.VELOCITY -> {
                workingValue = (finalValue * VELOCITY_PRECISION_TO_WORKING).toInt()
            }
            Value.TPL_PUSH_PULL -> {
                workingValue = (finalValue * TPL_PUSH_PULL_PRECISION_TO_WORKING).toInt()
            }
            Value.TPL_UP_DOWN -> {
                workingValue = (finalValue * TPL_UP_DOWN_PRECISION_TO_WORKING).toInt()
            }
            Value.COLOUR -> {
                workingValue = finalValue.toInt()
            }
            Value.PTO -> {
                workingValue = finalValue.toInt()
            }
        }

        return workingValue
    }

    fun convertWorkingValueToFinalValue(workingValue: Int, value: Value): Double {
        var finalValue = 0.0
        when (value) {
            Value.VELOCITY -> {
                finalValue = (workingValue * VELOCITY_PRECISION_TO_FINAL)
            }
            Value.TPL_PUSH_PULL -> {
                finalValue = (workingValue * TPL_PUSH_PULL_PRECISION_TO_FINAL)
            }
            Value.TPL_UP_DOWN -> {
                finalValue = (workingValue * TPL_UP_DOWN_PRECISION_TO_FINAL)
            }
            Value.COLOUR -> {
                finalValue = workingValue.toDouble()
            }
            Value.PTO -> {
                finalValue = workingValue.toDouble()
            }
        }

        return finalValue
    }

    fun convertSliderValueToDatabaseValue(sliderValue: Double, value: Value): Double {
        var databaseValue = 0.0
        when (value) {
            Value.VELOCITY -> {
                databaseValue =
                    convertKilometersPerHourToMetersPerSecond((sliderValue * VELOCITY_PRECISION_TO_FINAL))
            }
            Value.TPL_PUSH_PULL -> {
                databaseValue = (sliderValue) * TPL_PUSH_PULL_PRECISION_TO_FINAL
            }
            Value.TPL_UP_DOWN -> {
                databaseValue =
                    convertDegreesToRadians(sliderValue) * TPL_UP_DOWN_PRECISION_TO_FINAL
            }
            Value.COLOUR -> {
                databaseValue = sliderValue
            }
            Value.PTO -> {
                databaseValue = sliderValue
            }
        }

        return databaseValue
    }

    fun convertDatabaseValueToSliderValue(
        databaseValue: Double,
        value: Value
    ): Int {
        var sliderValue = 0
        when (value) {
            Value.VELOCITY -> {
                sliderValue =
                    (convertMetersPerSecondToKilometersPerHour(databaseValue) * VELOCITY_PRECISION_TO_WORKING).toInt()
            }
            Value.TPL_PUSH_PULL -> {
                sliderValue = ((databaseValue) * TPL_PUSH_PULL_PRECISION_TO_WORKING).toInt()
            }
            Value.TPL_UP_DOWN -> {
                sliderValue =
                    (convertRadiansToDegrees(databaseValue) * TPL_UP_DOWN_PRECISION_TO_WORKING).toInt()
            }
            Value.COLOUR -> {
                sliderValue = databaseValue.toInt()
            }
            Value.PTO -> {
                sliderValue = databaseValue.toInt()
            }
        }

        return sliderValue
    }

    companion object {
        enum class Value { VELOCITY, TPL_PUSH_PULL, TPL_UP_DOWN, COLOUR, PTO }

        val MIN_VELOCITY = 0.0//convertMetersPerSecondToKilometersPerHour(0.0) // m/s
        val MAX_VELOCITY = 36.0//convertMetersPerSecondToKilometersPerHour(10.0) // m/s
        const val VELOCITY_PRECISION_TO_WORKING = 10
        const val VELOCITY_PRECISION_TO_FINAL = 0.1
        const val MIN_TPL_PUSH_PULL = 0.0 // m
        const val MAX_TPL_PUSH_PULL = 0.45 // m
        const val TPL_PUSH_PULL_PRECISION_TO_WORKING = 100
        const val TPL_PUSH_PULL_PRECISION_TO_FINAL = 0.01
        val MIN_TPL_UP_DOWN = convertRadiansToDegrees(-0.26) // rad
        val MAX_TPL_UP_DOWN = convertRadiansToDegrees(0.52) // rad
        const val TPL_UP_DOWN_PRECISION_TO_WORKING = 10
        const val TPL_UP_DOWN_PRECISION_TO_FINAL = 0.1
        const val MIN_COLOUR = 0.0 // %
        const val MAX_COLOUR = 100.0 // %
        const val MIN_PTO = 0.0 // RPM
        const val MAX_PTO = 1000.0 // RPM

        fun convertMetersPerSecondToKilometersPerHour(metersPerSecond: Double): Double {
            return (metersPerSecond * 36 / 10)
        }

        fun convertKilometersPerHourToMetersPerSecond(kilometersPerHour: Double): Double {
            return (kilometersPerHour * 10 / 36)
        }

        fun convertRadiansToDegrees(radians: Double): Double {
            return (radians * 180 / PI)
        }

        fun convertDegreesToRadians(degrees: Double): Double {
            return (degrees * PI / 180)
        }
    }
}
