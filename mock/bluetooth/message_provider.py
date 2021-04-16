import random


def get_agribot_diagnosticData():
    array = bytearray(14)
    array[0] = 67
    array[1] = 10
    array[2] = 10
    array[3] = 10
    array[4] = 100
    array[5] = random.randint(3, 50)  # RPM 1
    array[6] = random.randint(3, 50)  # RPM 2
    array[7] = random.randint(3, 99)  # Torque of engine
    array[8] = random.randint(3, 99)  # Fuel Rate
    array[9] = random.randint(3, 99)  # Fuel Level
    array[10] = random.randint(3, 99)  # Fuel temperature
    array[11] = random.randint(65, 99)  # Accumulator 1
    array[12] = random.randint(60, 99)  # Accumulator 2
    array[13] = 13
    return bytes(array)


def get_agribot_handshake():
    array = bytearray(8)
    array[0] = 67
    array[1] = 11
    array[2] = 0
    array[3] = 0
    array[4] = 11
    array[5] = 1
    array[6] = 77
    array[7] = 13
    return bytes(array)


