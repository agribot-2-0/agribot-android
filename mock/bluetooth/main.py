

import time

import bluetooth

from message_constant import FrameConstant, MessageCommand
from message_manager import MessageManager
from message_provider import get_agribot_handshake, get_agribot_diagnostic_data, \
    get_agribot_hydraulic_data


def find_target(target_name):
    target_address = None
    nearby_devices = bluetooth.discover_devices()

    for bluetooth_device in nearby_devices:
        if target_name == bluetooth.lookup_name(bluetooth_device):
            target_address = bluetooth_device
            break

    return target_address


def run_server():
    server_socket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    server_socket.bind(("", 1))
    server_socket.listen(1)

    port = server_socket.getsockname()[1]
    print("listening on port %d" % port)

    client_socket, address = server_socket.accept()
    print("Accepted connection from ", address)
    return server_socket, client_socket

def send_message(message):
    CLIENT_SOCKET.send(message)


def handle_typical_message(message):
    MessageManager.log_mobile_app_message(message)
    send_message(message)
    MessageManager.log_server_message(message)
    send_diagnostic_data()


def send_diagnostic_data():
    time.sleep(1)
    send_message(get_agribot_diagnostic_data())
    time.sleep(1)
    send_message(get_agribot_hydraulic_data())


def response_to_handshake(handshake):
    MessageManager.log_mobile_app_message(handshake)
    send_message(get_agribot_handshake())
    MessageManager.log_server_message(handshake)


SERVER_SOCKET, CLIENT_SOCKET = run_server()

while 1:
    DATA = CLIENT_SOCKET.recv(FrameConstant.BUFFER_SIZE.value)
    print(DATA)

    if DATA:
        if MessageManager.get_message_command(
                DATA) == MessageCommand.MOBILE_APP_HANDSHAKE.value:
            response_to_handshake(DATA)
        else:
            handle_typical_message(DATA)
        # Echo back to client

CLIENT_SOCKET.close()
SERVER_SOCKET.close()
