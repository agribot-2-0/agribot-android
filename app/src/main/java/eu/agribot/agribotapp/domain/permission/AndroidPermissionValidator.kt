package eu.agribot.agribotapp.domain.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi

class AndroidPermissionValidator(val context: Context) :
    PermissionValidator {

    override fun hasLocationPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            hasLocationPermissionWithBackground()
        } else {
            hasLocationPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun hasLocationPermissionWithBackground() =
        context.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun hasLocationPermission() =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}
