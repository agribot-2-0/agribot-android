package eu.agribot.agribotapp.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi

class AndroidPermissionValidator(val context: Context) : PermissionValidator {
    override fun hasLocationPermissions(): Boolean {
        TODO("Not yet implemented")
    }


}
