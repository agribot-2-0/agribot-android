from __future__ import print_function
import time

import threading
import bluetooth

from message_constant import FrameConstant, MessageCommand
from message_manager import MessageManager, get_message_command
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
    print("Waiting for connection with Agribot Mobile App on port {}".format(
        port))

    client_socket, address = server_socket.accept()
    print("Accepted connection from {} on port {}".format(
        address[0], address[1]))
    return server_socket, client_socket


def send_message(message):
    CLIENT_SOCKET.send(message)


def handle_typical_message(message):
    # MessageManager.log_mobile_app_message(message)
    send_message(message)
    # MessageManager.log_server_message(message)
    # send_diagnostic_data()


def send_diagnostic_data():
    while True:
        send_message(get_agribot_diagnostic_data())
        time.sleep(0.01)
        send_message(get_agribot_hydraulic_data())
        time.sleep(1)


def response_to_handshake(handshake):
    # MessageManager.log_mobile_app_message(handshake)
    send_message(get_agribot_handshake())
    MessageManager.log_server_message(get_agribot_handshake())


def keep_connection():
    while True:
        data_received = CLIENT_SOCKET.recv(FrameConstant.BUFFER_SIZE.value)
        # print(data_received)

        if data_received:
            if get_message_command(
                    data_received
            ) == MessageCommand.MOBILE_APP_HANDSHAKE.value:
                response_to_handshake(data_received)
            else:
                handle_typical_message(data_received)
            # Echo back to client


def main():

    handle_heartbeat_thread = threading.Thread(target=keep_connection)
    send_data_thread = threading.Thread(target=send_diagnostic_data)

    handle_heartbeat_thread.start()
    send_data_thread.start()

    handle_heartbeat_thread.join()
    send_data_thread.join()


SERVER_SOCKET, CLIENT_SOCKET = run_server()
main()
CLIENT_SOCKET.close()
SERVER_SOCKET.close()
