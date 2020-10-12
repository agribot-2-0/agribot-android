package eu.agribot.agribotapp.domain.permission

interface PermissionManager {
    fun requestPermission(
        onSuccess: () -> Unit,
        onError: (PermissionError) -> Unit
    )
}
