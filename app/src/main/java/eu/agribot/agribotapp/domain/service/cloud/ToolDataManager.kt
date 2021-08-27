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
                pto = convertSliderValueToDatabaseValue(sliderValue.toDouble(), Value.PTO)
            }
            ToolRecylerItemId.TPL_UP_DOWN.ID -> {
                tplUpDown =
                    convertSliderValueToDatabaseValue(sliderValue.toDouble(), Value.TPL_UP_DOWN)
            }
            ToolRecylerItemId.TPL_PUSH_PULL.ID -> {
                tplPushPull =
                    convertSliderValueToDatabaseValue(sliderValue.toDouble(), Value.TPL_PUSH_PULL)
            }
            ToolRecylerItemId.GREEN.ID -> {
                green = convertSliderValueToDatabaseValue(sliderValue.toDouble(), Value.COLOUR)
            }
            ToolRecylerItemId.YELLOW.ID -> {
                yellow = convertSliderValueToDatabaseValue(sliderValue.toDouble(), Value.COLOUR)
            }
            ToolRecylerItemId.BLUE.ID -> {
                blue = convertSliderValueToDatabaseValue(sliderValue.toDouble(), Value.COLOUR)
            }
            ToolRecylerItemId.V_MAX.ID -> {
                vMax = convertSliderValueToDatabaseValue(sliderValue.toDouble(), Value.VELOCITY)
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
                minValue = convertDatabaseValueToSliderValue(MIN_VELOCITY, Value.VELOCITY),
                maxValue = convertDatabaseValueToSliderValue(MAX_VELOCITY, Value.VELOCITY),
                precision = VELOCITY_PRECISION
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
                minValue = convertDatabaseValueToSliderValue(MIN_COLOUR, Value.COLOUR),
                maxValue = convertDatabaseValueToSliderValue(MAX_COLOUR, Value.COLOUR)
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
                minValue = convertDatabaseValueToSliderValue(MIN_COLOUR, Value.COLOUR),
                maxValue = convertDatabaseValueToSliderValue(MAX_COLOUR, Value.COLOUR)
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
                minValue = convertDatabaseValueToSliderValue(MIN_COLOUR, Value.COLOUR),
                maxValue = convertDatabaseValueToSliderValue(MAX_COLOUR, Value.COLOUR)
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
                minValue = convertDatabaseValueToSliderValue(MIN_PTO, Value.PTO),
                maxValue = convertDatabaseValueToSliderValue(MAX_PTO, Value.PTO)
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
                minValue = convertDatabaseValueToSliderValue(
                    MIN_TPL_PUSH_PULL,
                    Value.TPL_PUSH_PULL
                ),
                maxValue = convertDatabaseValueToSliderValue(MAX_TPL_PUSH_PULL, Value.TPL_PUSH_PULL),
                precision = TPL_PUSH_PULL_PRECISION
            )
        )
    }

    fun getRobotTplUpDownFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.TPL_UP_DOWN.ID,
            toolName = resourcesProvider.getString(R.string.tpl_up_down),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = convertDatabaseValueToSliderValue(tplUpDown, Value.TPL_UP_DOWN),
                toolUnit = resourcesProvider.getString(R.string.degree),
                minValue = convertDatabaseValueToSliderValue(MIN_TPL_UP_DOWN, Value.TPL_UP_DOWN),
                maxValue = convertDatabaseValueToSliderValue(MAX_TPL_UP_DOWN, Value.TPL_UP_DOWN),
                precision = TPL_UP_DOWN_PRECISION
            )
        )
    }

    fun convertSliderValueToDatabaseValue(sliderValue: Double, value: Value): Double {
        var databaseValue = 0.0
        when (value) {
            Value.VELOCITY -> {
                databaseValue =
                    convertKilometersPerHourToMetersPerSecond((sliderValue * VELOCITY_PRECISION))
            }
            Value.TPL_PUSH_PULL -> {
                databaseValue = (sliderValue) * TPL_PUSH_PULL_PRECISION
            }
            Value.TPL_UP_DOWN -> {
                databaseValue =
                    convertDegreesToRadians(sliderValue) * TPL_UP_DOWN_PRECISION
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
                    (convertMetersPerSecondToKilometersPerHour(databaseValue) / VELOCITY_PRECISION).toInt()
            }
            Value.TPL_PUSH_PULL -> {
                sliderValue = ((databaseValue) / TPL_PUSH_PULL_PRECISION).toInt()
            }
            Value.TPL_UP_DOWN -> {
                sliderValue =
                    (convertRadiansToDegrees(databaseValue) / TPL_UP_DOWN_PRECISION).toInt()
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

        const val MIN_VELOCITY = 0.0 // m/s
        const val MAX_VELOCITY = 3.0 // m/s
        const val VELOCITY_PRECISION = 0.1
        const val MIN_TPL_PUSH_PULL = 0.0 // m
        const val MAX_TPL_PUSH_PULL = 0.45 // m
        const val TPL_PUSH_PULL_PRECISION = 0.01
        const val MIN_TPL_UP_DOWN = -0.26 // rad
        const val MAX_TPL_UP_DOWN = 0.52 // rad
        const val TPL_UP_DOWN_PRECISION = 0.1
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
