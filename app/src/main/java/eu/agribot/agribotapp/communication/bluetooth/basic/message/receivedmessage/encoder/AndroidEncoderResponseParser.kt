package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder

import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.ToolFeedbackDataBD
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
        return if (encoderResponse.size == TPL_FRAME_LENGTH) {
            val toolValue = encoderResponse[CONF_VAL_B1_POSITION].toInt()
                .shl(BYTE_LENGHT) or encoderResponse[CONF_VAL_B0_POSITION].toInt()
            ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
        } else {
            ToolFeedbackDataBD()
        }
    }

    private fun getGreenToolValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        return if (encoderResponse.size == TPL_FRAME_LENGTH) {
            val toolValue = encoderResponse[CONF_VAL_B0_POSITION].toInt()
            ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
        } else {
            ToolFeedbackDataBD()
        }
    }

    private fun getYellowToolValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        return if (encoderResponse.size == TPL_FRAME_LENGTH) {
            val toolValue = encoderResponse[CONF_VAL_B0_POSITION].toInt()
            ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
        } else {
            ToolFeedbackDataBD()
        }
    }

    private fun getBlueToolValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        return if (encoderResponse.size == TPL_FRAME_LENGTH) {
            val toolValue = encoderResponse[CONF_VAL_B0_POSITION].toInt()
            ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
        } else {
            ToolFeedbackDataBD()
        }
    }

    private fun getTplAngleValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        return if (encoderResponse.size == TPL_FRAME_LENGTH) {
            if (isNegativeNumber(encoderResponse[CONF_VAL_B1_POSITION])) {
                var toolValue = changeHighestBit(encoderResponse[CONF_VAL_B1_POSITION]).toInt()
                    .shl(BYTE_LENGHT) + encoderResponse[CONF_VAL_B0_POSITION].toInt()
                toolValue *= -1
                ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
            } else {
                val toolValue = encoderResponse[CONF_VAL_B1_POSITION].toInt()
                    .shl(BYTE_LENGHT) + encoderResponse[CONF_VAL_B0_POSITION].toInt()
                ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
            }
        } else {
            ToolFeedbackDataBD()
        }
    }

    private fun getTplHeightValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        return if (encoderResponse.size == TPL_FRAME_LENGTH) {
            if (isNegativeNumber(encoderResponse[CONF_VAL_B1_POSITION])) {
                var toolValue = changeHighestBit(encoderResponse[CONF_VAL_B1_POSITION]).toInt()
                    .shl(BYTE_LENGHT) + encoderResponse[CONF_VAL_B0_POSITION].toInt()
                toolValue *= -1
                ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
            } else {
                val toolValue = encoderResponse[CONF_VAL_B1_POSITION].toInt()
                    .shl(BYTE_LENGHT) + encoderResponse[CONF_VAL_B0_POSITION].toInt()
                ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
            }
        } else {
            ToolFeedbackDataBD()
        }
    }

    private fun changeHighestBit(number: Byte): Byte {
        return number.xor(HIGHEST_BIT_MASK.toByte())
    }

    private fun isNegativeNumber(number: Byte): Boolean {
        return (number.takeHighestOneBit() != 0.toByte())
    }

    private fun twoBytesToUnsingedInt(array: ByteArray): Int {
        var result = 0
        if (array.size == 2) {
            result = array[0].toInt().shl(BYTE_LENGHT) + array[1].toInt()
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
        const val BYTE_LENGHT = 8
        const val HIGHEST_BIT_MASK = 0x80
    }
}
