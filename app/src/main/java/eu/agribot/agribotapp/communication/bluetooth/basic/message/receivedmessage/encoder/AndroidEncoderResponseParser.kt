package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder

import eu.agribot.agribotapp.communication.bluetooth.basic.message.frame.data.FrameConstant
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.ConstantReceivedMessage
import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.ToolFeedbackDataBD
import timber.log.Timber
import kotlin.experimental.xor

class AndroidEncoderResponseParser : EncoderResponseParser {

    override fun parseEncoderResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        return when (encoderResponse[DiagnosticDataArrayPosition.TOOL_FEEDBACK_ID.position].toInt()) {
            Tools.TplAngle.Front.toolId -> {
                getTplAngleValueResponse(encoderResponse)
            }
            Tools.TplAngle.Rear.toolId -> {
                getTplAngleValueResponse(encoderResponse)
            }
            Tools.TplHeight.Front.toolId -> {
                getTplHeightValueResponse(encoderResponse)
            }
            Tools.TplHeight.Rear.toolId -> {
                getTplHeightValueResponse(encoderResponse)
            }
            Tools.Pto.Front.toolId -> {
                getPtoValueResponse(encoderResponse)
            }
            Tools.Pto.Rear.toolId -> {
                getPtoValueResponse(encoderResponse)
            }
            Tools.Green.Front.toolId -> {
                getGreenToolValueResponse(encoderResponse)
            }
            Tools.Green.Rear.toolId -> {
                getGreenToolValueResponse(encoderResponse)
            }
            Tools.Yellow.Front.toolId -> {
                getYellowToolValueResponse(encoderResponse)
            }
            Tools.Yellow.Rear.toolId -> {
                getYellowToolValueResponse(encoderResponse)
            }
            Tools.Blue.Front.toolId -> {
                getBlueToolValueResponse(encoderResponse)
            }
            Tools.Blue.Rear.toolId -> {
                getBlueToolValueResponse(encoderResponse)
            }
            else -> ToolFeedbackDataBD()
        }
    }

    private fun getPtoValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        var toolValue = TOOL_INIT_VALUE
        if (encoderResponse.size == TPL_FRAME_LENGTH) {
            toolValue = encoderResponse[CONF_VAL_B1_POSITION].toInt()
                .shl(PTO_SHL_VALUE) or encoderResponse[CONF_VAL_B0_POSITION].toInt()
        }
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
    }

    private fun getGreenToolValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        var toolValue = TOOL_INIT_VALUE
        if (encoderResponse.size == TPL_FRAME_LENGTH) {
            toolValue = encoderResponse[CONF_VAL_B0_POSITION].toInt()
        }
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
    }

    private fun getYellowToolValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        var toolValue = TOOL_INIT_VALUE
        if (encoderResponse.size == TPL_FRAME_LENGTH) {
            toolValue = encoderResponse[CONF_VAL_B0_POSITION].toInt()
        }
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
    }

    private fun getBlueToolValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        var toolValue = TOOL_INIT_VALUE
        if (encoderResponse.size == TPL_FRAME_LENGTH) {
            toolValue = encoderResponse[CONF_VAL_B0_POSITION].toInt()
        }
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
    }

    private fun getTplAngleValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        var toolValue = TOOL_INIT_VALUE
        if (encoderResponse.size == TPL_FRAME_LENGTH) {
            if (isNegativeNumber(encoderResponse[CONF_VAL_B1_POSITION])) {
                toolValue = changeHighestBit(encoderResponse[CONF_VAL_B1_POSITION]).toInt()
                    .shl(PTO_SHL_VALUE) + encoderResponse[CONF_VAL_B0_POSITION].toInt()
                toolValue *= -1
            }
            else
            {
                toolValue = encoderResponse[CONF_VAL_B1_POSITION].toInt()
                    .shl(PTO_SHL_VALUE) + encoderResponse[CONF_VAL_B0_POSITION].toInt()
            }
        }
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
    }

    private fun getTplHeightValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        var toolValue = TOOL_INIT_VALUE
        if (encoderResponse.size == TPL_FRAME_LENGTH) {
            if (isNegativeNumber(encoderResponse[CONF_VAL_B1_POSITION])) {
                toolValue = changeHighestBit(encoderResponse[CONF_VAL_B1_POSITION]).toInt()
                    .shl(PTO_SHL_VALUE) + encoderResponse[CONF_VAL_B0_POSITION].toInt()
                toolValue *= -1
            }
            else
            {
                toolValue = encoderResponse[CONF_VAL_B1_POSITION].toInt()
                    .shl(PTO_SHL_VALUE) + encoderResponse[CONF_VAL_B0_POSITION].toInt()
            }
        }
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
    }

    private fun changeHighestBit(number: Byte): Byte {
        return number.xor(128.toByte())
    }

    private fun isNegativeNumber(number: Byte): Boolean {
        return (number.takeHighestOneBit() != 0.toByte())
    }

    private fun twoBytesToUnsingedInt(array: ByteArray): Int {
        var result = 0
        if(array.size == 2) {
            array[0].toInt().shl(8) + array[1].toInt()
        }

        return result
    }

    private fun getToolId(encoderResponse: ByteArray): Int =
        encoderResponse[CONF_ID_POSITION].toInt()

    companion object {
        const val CONF_ID_POSITION = 0
        const val CONF_VAL_B0_POSITION = 2
        const val CONF_VAL_B1_POSITION = 1
        const val TPL_FRAME_LENGTH = 3
        const val PTO_SHL_VALUE = 8
        const val TOOL_INIT_VALUE = -1
    }
}
