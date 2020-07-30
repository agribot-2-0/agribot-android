package eu.agribot.agribotapp.permissions

import android.content.Context

class AndroidPermissionManager(val context: Context) : PermissionManager {
    override fun requestLocationPermission(
        onSuccess: () -> Unit,
        onError: (PermissionError) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}
