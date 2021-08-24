package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePath.TOOL_PHOTO_FILE_TYPE
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.HitchListItemDetail
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.ToolRecylerItemId
import timber.log.Timber

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
        Timber.d("tool updateAllToolParameters" + toolDetails.toString())
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
                toolStartValue = vMax.toInt(),
                toolUnit = "km/h",
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
                toolStartValue = green.toInt(),
                toolUnit = "%",
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
                toolStartValue = yellow.toInt(),
                toolUnit = "%",
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
                toolStartValue = blue.toInt(),
                toolUnit = "%",
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
                toolStartValue = pto.toInt(),
                toolUnit = "RPM",
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
                toolStartValue = tplPushPull.toInt(),
                toolUnit = "m",
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
                toolStartValue = tplUpDown.toInt(),
                toolUnit = "rad",
                minValue = convertFinalValueToWorkingValue(MIN_TPL_UP_DOWN, Value.TPL_UP_DOWN),
                maxValue = convertFinalValueToWorkingValue(MAX_TPL_UP_DOWN, Value.TPL_UP_DOWN)
            )
        )
    }

    private fun convertFinalValueToWorkingValue(finalValue: Double, value: Value): Int {
        var workingValue = 0
        when (value) {
            Value.VELOCITY -> {
                workingValue = (finalValue * 10).toInt()
            }
            Value.TPL_PUSH_PULL -> {
                workingValue = (finalValue * 100).toInt()
            }
            Value.TPL_UP_DOWN -> {
                workingValue = (finalValue * 100).toInt()
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

    private fun convertSliderValueToDatabaseValue(sliderValue: Int, value: Value): Double {
        var databaseValue = 0.0
        when (value) {
            Value.VELOCITY -> {
                databaseValue = ((sliderValue.toDouble()) * 0.1) * 10 / 36
            }
            Value.TPL_PUSH_PULL -> {
                databaseValue = (sliderValue.toDouble()) * 0.01
            }
            Value.TPL_UP_DOWN -> {
                databaseValue = (sliderValue.toDouble()) * 0.01
            }
            Value.COLOUR -> {
                databaseValue = sliderValue.toDouble()
            }
            Value.PTO -> {
                databaseValue = sliderValue.toDouble()
            }
        }

        return databaseValue
    }

    companion object {
        enum class Value { VELOCITY, TPL_PUSH_PULL, TPL_UP_DOWN, COLOUR, PTO }
        const val MIN_VELOCITY = 0.0 // km/h
        const val MAX_VELOCITY = 10.0 // km/h
        const val MIN_TPL_PUSH_PULL = 0.0 // m
        const val MAX_TPL_PUSH_PULL = 0.45 // m
        const val MIN_TPL_UP_DOWN = -0.26 // rad
        const val MAX_TPL_UP_DOWN = 0.52 // rad
        const val MIN_COLOUR = 0.0 // %
        const val MAX_COLOUR = 100.0 // %
        const val MIN_PTO = 0.0 // RPM
        const val MAX_PTO = 1000.0 // RPM
    }
}
