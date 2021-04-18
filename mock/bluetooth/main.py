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
    server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    server_sock.bind(("", 1))
    server_sock.listen(1)

    port = server_sock.getsockname()[1]
    print("listening on port %d" % port)

    client_sock, address = server_sock.accept()
    print("Accepted connection from ", address)
    return server_sock, client_sock


server_sock, client_sock = run_server()


def send_message(message):
    client_sock.send(message)


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


while 1:
    data = client_sock.recv(FrameConstant.BUFFER_SIZE.value)
    print(data)

    if data:
        if MessageManager.get_message_command(
                data) == MessageCommand.MOBILE_APP_HANDSHAKE.value:
            response_to_handshake(data)
        else:
            handle_typical_message(data)
        # Echo back to client

client_sock.close()
server_sock.close()
