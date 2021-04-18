import random


def get_agribot_diagnosticData():
    array = bytearray(12)
    array[0] = 67
    array[1] = 11
    array[2] = 0
    array[3] = 0
    array[4] = 100
    array[5] = 1
    array[6] = random.randint(1, 100)
    array[7] = random.randint(1, 100)
    array[8] = random.randint(1, 100)
    array[9] = random.randint(1, 100)
    array[10] = random.randint(1, 100)
    array[11] = 13
    return bytes(array)


def get_agribot_hydraulic_data():
    array = bytearray(12)
    array[0] = 67
    array[1] = 11
    array[2] = 0
    array[3] = 0
    array[4] = 105
    array[5] = 1
    array[6] = random.randint(1, 100)
    array[7] = random.randint(1, 100)
    array[8] = random.randint(1, 100)
    array[9] = random.randint(1, 100)
    array[10] = random.randint(1, 100)
    array[11] = 13
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
