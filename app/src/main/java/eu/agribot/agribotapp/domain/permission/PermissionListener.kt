package eu.agribot.agribotapp.domain.permission

import android.Manifest
import android.os.Build
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class PermissionListener(
    private val onSuccess: () -> Unit,
    private val onError: (PermissionError) -> Unit
) : MultiplePermissionsListener {

    override fun onPermissionRationaleShouldBeShown(
        permissions: MutableList<PermissionRequest>,
        token: PermissionToken
    ) = token.continuePermissionRequest()

    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        checkPermissionReport(report)
    }

    private fun checkPermissionReport(report: MultiplePermissionsReport) = when {
        hasPermanentlyDeniedLocation(report) ->
            onError(PermissionError.LOCATION_DENIED_PERMANENTLY)
        hasDeniedLocation(report) ->
            onError(PermissionError.LOCATION_DENIED)
        hasPermanentlyDeniedBackgroundLocation(report) ->
            onError(PermissionError.BACKGROUND_LOCATION_DENIED_PERMANENTLY)
        hasDeniedBackgroundLocation(report) ->
            onError(PermissionError.BACKGROUND_LOCATION_DENIED)
        hasPermamentlyDeniedBluetooth(report) ->
            onError(PermissionError.BLUETOOTH_DENIED_PERMANENTLY)
        hasDeniedBluetooth(report) ->
            onError(PermissionError.BLUETOOTH_DENIED)
        hasPermamentlyDeniedBluetoothAdmin(report) ->
            onError(PermissionError.BLUETOOTH_DENIED_PERMANENTLY)
        hasDeniedBluetoothAdmin(report) ->
            onError(PermissionError.BLUETOOTH_DENIED)
        hasDeniedCamera(report) ->
            onError(PermissionError.CAMERA_DENIED)
        else -> onSuccess.invoke()
    }

    private fun hasPermamentlyDeniedBluetoothAdmin(report: MultiplePermissionsReport): Boolean {
        return report.deniedPermissionResponses.any {
            it.permissionName == Manifest.permission.BLUETOOTH_ADMIN && it.isPermanentlyDenied
        }
    }

    private fun hasDeniedBluetoothAdmin(report: MultiplePermissionsReport): Boolean {
        return report.deniedPermissionResponses.any {
            it.permissionName == Manifest.permission.BLUETOOTH_ADMIN
        }
    }

    private fun hasPermamentlyDeniedBluetooth(report: MultiplePermissionsReport): Boolean {
        return report.deniedPermissionResponses.any {
            it.permissionName == Manifest.permission.BLUETOOTH && it.isPermanentlyDenied
        }
    }

    private fun hasDeniedBluetooth(report: MultiplePermissionsReport): Boolean {
        return report.deniedPermissionResponses.any {
            it.permissionName == Manifest.permission.BLUETOOTH
        }
    }

    private fun hasPermanentlyDeniedBackgroundLocation(report: MultiplePermissionsReport): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) false
        else report.deniedPermissionResponses.any {
            it.permissionName == Manifest.permission.ACCESS_BACKGROUND_LOCATION && it.isPermanentlyDenied
        }
    }

    private fun hasPermanentlyDeniedLocation(report: MultiplePermissionsReport): Boolean {
        return report.deniedPermissionResponses.any {
            it.permissionName == Manifest.permission.ACCESS_FINE_LOCATION && it.isPermanentlyDenied
        }
    }

    private fun hasDeniedBackgroundLocation(report: MultiplePermissionsReport): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) false
        else report.deniedPermissionResponses.any {
            it.permissionName == Manifest.permission.ACCESS_BACKGROUND_LOCATION
        }
    }

    private fun hasDeniedLocation(report: MultiplePermissionsReport): Boolean {
        return report.deniedPermissionResponses.any {
            it.permissionName == Manifest.permission.ACCESS_FINE_LOCATION
        }
    }

    private fun hasDeniedCamera(report: MultiplePermissionsReport): Boolean {
        return report.deniedPermissionResponses.any {
            it.permissionName == Manifest.permission.CAMERA
        }
    }
}
