package eu.agribot.agribotapp.domain.permission

interface PermissionValidator {
    fun hasLocationPermissions(): Boolean

    fun hasBluetoothPermissions(): Boolean

    fun hasBluetoothAdminPermissions(): Boolean

    fun hasCameraPermissions(): Boolean
}
