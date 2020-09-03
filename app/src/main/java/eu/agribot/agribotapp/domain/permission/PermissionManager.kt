package eu.agribot.agribotapp.domain.permission

interface PermissionManager {

    fun requestLocationPermission(
        onSuccess: () -> Unit,
        onError: () -> Unit
    )
}