from message_constant import MessageCommand, MessageArrayPosition, FrameConstant, MessageDirection
from datetime import datetime

from message_provider import get_agribot_diagnosticData, get_agribot_handshake


def get_message_length(message):
    if len(message) != 0:
        return message[MessageArrayPosition.LENGTH_POSITION.value]

def get_message_command(message):
    global first_command_element, second_command_element
    if get_message_length(message) > 2:
        first_command_element = message[
                                    MessageArrayPosition.FIRST_BYTE_OF_ADDRESS_POSITION.value] << FrameConstant.BIT_SHIFT.value
        if message[MessageArrayPosition.SECOND_BYTE_OF_ADDRESS_POSITION.value] < FrameConstant.EMPTY_ARRAY.value:
            second_command_element = FrameConstant.ADDRESS_FLAG.value + message[
                MessageArrayPosition.SECOND_BYTE_OF_ADDRESS_POSITION.value]
        else:
            second_command_element = message[MessageArrayPosition.SECOND_BYTE_OF_ADDRESS_POSITION.value]
    message_command = first_command_element + second_command_element
    return message_command

def get_message_command_name(message):
    switcher = {
        MessageCommand.HEARTBEAT.value: "HEARTBEAT",
        MessageCommand.MOBILE_APP_HANDSHAKE.value: "MOBILEAPP_HANDSHAKE",
        MessageCommand.AGRIBOT_HANDSHAKE.value: "AGRIBOT_HANDSHAKE",
        MessageCommand.MANUAL_MODE_INITIALIZATION_MESSAGE.value: "MANUAL_INIT",
        MessageCommand.MANUAL_MODE_RUN_MESSAGE.value: "MANUAL_MODE_RUN_MESSAGE",
        MessageCommand.AUTO_START.value: "AUTO_START",
        MessageCommand.AGRIBOT_ERROR.value: "AGRIBOT_ERROR",
        MessageCommand.START_ENGINE.value: "START_ENGINE",
        MessageCommand.STOP_SIGNAL.value: "STOP_SIGNAL",
        MessageCommand.MOVE_FORWARD_BACKWARD.value: "MOVE_FORWARD_BACKWARD",
        MessageCommand.ROTATE_CARRIAGES.value: "ROTATE_CARRIAGES",
        MessageCommand.RIDE_THE_CURVE.value: "RIDE_THE_CURVE",
        MessageCommand.AGRIBOT_SET_SWITCH.value: "AGRIBOT_SET_SWITCH"

    }
    return "[" + switcher.get(message, "Undefined Command") + "]"


def get_message_data(message):
    slice_message = message[5:len(message) - 2]
    string_to_return = " [ "
    bytes_str = ''.join('0x{:02x} '.format(x) for x in slice_message)
    string_to_return += bytes_str
    string_to_return += " ] "
    return string_to_return


def get_actual_time():
    actual_date = datetime.now()
    actual_time = "{0} : {1} : {2} : {3}".format(str(actual_date.hour), str(actual_date.minute),
                                                 str(actual_date.second), str(actual_date.microsecond))
    return "[ " + actual_time + ' ] '


class MessageManager:

    def get_message_command(message):
        global first_command_element, second_command_element
        if get_message_length(message) > 2:
            first_command_element = message[
                                        MessageArrayPosition.FIRST_BYTE_OF_ADDRESS_POSITION.value] << FrameConstant.BIT_SHIFT.value
            if message[MessageArrayPosition.SECOND_BYTE_OF_ADDRESS_POSITION.value] < FrameConstant.EMPTY_ARRAY.value:
                second_command_element = FrameConstant.ADDRESS_FLAG.value + message[
                    MessageArrayPosition.SECOND_BYTE_OF_ADDRESS_POSITION.value]
            else:
                second_command_element = message[MessageArrayPosition.SECOND_BYTE_OF_ADDRESS_POSITION.value]
        message_command = first_command_element + second_command_element
        return message_command

    def log_mobile_app_message(message):
        print(get_actual_time() + MessageDirection.CLIENT.value + get_message_command_name(
            get_message_command(message)) + get_message_data(message))

    def log_server_message(message):
        print(get_actual_time() + MessageDirection.SERVER.value + get_message_command_name(get_message_command(message)) +
              get_message_data(message))
