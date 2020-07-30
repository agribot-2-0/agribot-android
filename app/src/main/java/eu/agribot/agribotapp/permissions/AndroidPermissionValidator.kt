package eu.agribot.agribotapp.permissions

import android.content.Context

class AndroidPermissionValidator(val context: Context) : PermissionValidator {
    override fun hasLocationPermissions(): Boolean {
        TODO("Not yet implemented")
    }
}
