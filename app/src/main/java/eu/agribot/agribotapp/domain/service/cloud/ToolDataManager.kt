package eu.agribot.agribotapp.domain.service.cloud

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.model.cdb.ToolDetails
import eu.agribot.agribotapp.domain.service.cloud.downloader.FilePath.TOOL_PHOTO_FILE_TYPE
import eu.agribot.agribotapp.ui.fragment.toolcontrol.data.HitchListItemDetail
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.ToolRecylerItemId

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
                toolStartValue = vMax.toInt()
            )
        )
    }

    fun getRobotGreenFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.GREEN.ID,
            toolName = resourcesProvider.getString(R.string.green_flow),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = green.toInt()
            )
        )
    }

    fun getRobotYellowFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.YELLOW.ID,
            toolName = resourcesProvider.getString(R.string.yellow_flow),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = yellow.toInt()
            )
        )
    }

    fun getRobotBlueFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.BLUE.ID,
            toolName = resourcesProvider.getString(R.string.blue_flow),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = blue.toInt()
            )
        )
    }

    fun getRobotPtoFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.PTO.ID,
            toolName = resourcesProvider.getString(R.string.pto),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = pto.toInt()
            )
        )
    }

    fun getRobotTplPushPullFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.TPL_PUSH_PULL.ID,
            toolName = resourcesProvider.getString(R.string.tpl_push_pull),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = tplPushPull.toInt()
            )
        )
    }

    fun getRobotTplUpDownFlow(): HitchListItemViewModel {
        return HitchListItemViewModel(
            switchNumber = ToolRecylerItemId.TPL_UP_DOWN.ID,
            toolName = resourcesProvider.getString(R.string.tpl_up_down),
            hitchListItemDetail = HitchListItemDetail(
                toolStartValue = tplUpDown.toInt()
            )
        )
    }
}
