How to connect PC with Android Phone/Tablet
Idea: Firstly pair device and later connect them by rfcomm bluetooth profile.

(0) Change PC bluetooth name to 'CANwirelessSN1705079'
    Edit "PRETTY_HOSTNAME=CANwirelessSN1705079" in /etc/machine-info  file

Temporary hack for Agribot Android App version 1 (android app only connects to the devices which starts with 'CAN' name)
Not needed if you connect to version 2

1) Following https://askubuntu.com/questions/775303/unified-remote-bluetooth-could-not-connect-to-sdp

You need to run the bluetooth daemon in compatibility mode to provide deprecated command line interfaces. You're running Bluez5 and you need some Bluez4 functions. You can do this by editing this file

/etc/systemd/system/dbus-org.bluez.service and changing this line

ExecStart=/usr/lib/bluetooth/bluetoothd to this

ExecStart=/usr/lib/bluetooth/bluetoothd --compat
ExecStartPost=/bin/chmod 777 /var/run/sdp
ExecStartPost=/usr/bin/sdptool add SP

and then restarting bluetooth like this

sudo systemctl daemon-reload
sudo systemctl restart bluetooth


Line by line description
a) run bluetoothd in commanad line mode 
ExecStart=/usr/lib/bluetooth/bluetoothd --compat
b) give permmitions to sdp (service discovery protocol)
ExecStartPost=/bin/chmod 777 /var/run/sdp
c) add SP (serial port) service to sdp
ExecStartPost=/usr/bin/sdptool add SP

2) Turn on bluetooth
3) Pair PC with a tablet. Don't connect with tablet and run main.py python script (firstly install
sudo apt-get install bluetooth libbluetooth-dev
sudo python3 -m pip install pybluez)
```
python3 main.py
```
