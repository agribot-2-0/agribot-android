package eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.encoder

import eu.agribot.agribotapp.communication.bluetooth.basic.message.receivedmessage.data.DiagnosticDataArrayPosition
import eu.agribot.agribotapp.communication.bluetooth.model.fromcommunication.ToolFeedbackDataBD

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
        val toolValue = encoderResponse[CONF_VAL_B1_POSITION].toInt()
            .shl(PTO_SHL_VALUE) / encoderResponse[CONF_VAL_B0_POSITION].toInt()
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
    }

    private fun getGreenToolValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        val toolValue = encoderResponse[CONF_VAL_B0_POSITION].toInt()
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)

    }

    private fun getYellowToolValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        val toolValue = encoderResponse[CONF_VAL_B0_POSITION].toInt()
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
    }

    private fun getBlueToolValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        val toolValue = encoderResponse[CONF_VAL_B0_POSITION].toInt()
        return ToolFeedbackDataBD(getToolId(encoderResponse), toolValue)
    }

    private fun getTplAngleValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        //TODO
        return ToolFeedbackDataBD()
    }

    private fun getTplHeightValueResponse(encoderResponse: ByteArray): ToolFeedbackDataBD {
        //TODO
        return ToolFeedbackDataBD()

    }

    private fun getToolId(encoderResponse: ByteArray): Int =
        encoderResponse[TOOL_ID_POSITION].toInt()


    companion object {
        const val TOOL_ID_POSITION = 0
        const val CONF_VAL_B0_POSITION = 2
        const val CONF_VAL_B1_POSITION = 1
        const val PTO_SHL_VALUE = 8

    }

}