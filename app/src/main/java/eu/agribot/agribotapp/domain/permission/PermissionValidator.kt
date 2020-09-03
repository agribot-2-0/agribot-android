package eu.agribot.agribotapp.domain.permission

interface PermissionValidator {
        fun hasLocationPermissions(): Boolean

}