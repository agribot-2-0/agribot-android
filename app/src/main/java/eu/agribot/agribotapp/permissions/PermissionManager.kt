package eu.agribot.agribotapp.permissions

interface PermissionManager {

    fun requestLocationPermission(  onSuccess: () -> Unit,
                                    onError: (PermissionError) -> Unit)
}