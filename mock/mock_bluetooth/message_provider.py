import random
from message_constant import CanMessageBytes, MessageCommand


def uint16_to_two_bytes(data):
    return (data & 0xff00) >> 8, (data & 0x00ff)


def get_can_id(can_id):
    return uint16_to_two_bytes(can_id)


def encode_oil_temperature(oil_temperature):
    return oil_temperature + 80


def encode_accumulator_voltage(accumulator_voltage):
    accumulator_voltage *= 1000.0
    accumulator_voltage = int(accumulator_voltage)
    return uint16_to_two_bytes(accumulator_voltage)


def get_agribot_diagnostic_data():
    array = bytearray(14)
    array[0] = CanMessageBytes.START_OF_FRAME_BYTE.value
    array[1] = 11
    array[2] = CanMessageBytes.COMMAND_BYTE.value
    array[3], array[4] = get_can_id(
        MessageCommand.AGRIBOT_DIAGNOSTIC_DATA_1.value)
    array[5], array[6] = uint16_to_two_bytes(1200)
    array[7] = 33
    array[8] = 50
    array[9] = 80
    array[10] = 34
    array[11], array[12] = encode_accumulator_voltage(24.4)
    array[13] = CanMessageBytes.END_OF_FRAME_BYTE.value
    return bytes(array)


def get_agribot_hydraulic_data():
    array = bytearray(14)
    array[0] = CanMessageBytes.START_OF_FRAME_BYTE.value
    array[1] = 11
    array[2] = CanMessageBytes.COMMAND_BYTE.value
    array[3], array[4] = get_can_id(
        MessageCommand.AGRIBOT_HYDRAULICS_DATA.value)
    array[5] = 1
    array[6] = random.randint(1, 100)
    array[7] = encode_oil_temperature(55)
    array[8] = random.randint(1, 100)
    array[9] = random.randint(1, 100)
    array[10] = random.randint(1, 100)
    array[13] = CanMessageBytes.END_OF_FRAME_BYTE.value
    return bytes(array)


def get_agribot_handshake():
    array = bytearray(8)
    array[0] = CanMessageBytes.START_OF_FRAME_BYTE.value
    array[1] = 11
    array[2] = CanMessageBytes.COMMAND_BYTE.value
    array[3], array[4] = get_can_id(MessageCommand.AGRIBOT_HANDSHAKE.value)
    array[5] = 1
    array[6] = 77
    array[7] = CanMessageBytes.END_OF_FRAME_BYTE.value
    return bytes(array)
