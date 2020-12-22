package eu.agribot.agribotapp.domain.permission

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.karumi.dexter.Dexter

class AndroidPermissionManager(
    private val context: Context
) : PermissionManager {
    override fun requestPermission(
        onSuccess: () -> Unit,
        onError: (PermissionError) -> Unit
    ) {
        Dexter.withContext(context)
            .withPermissions(
                getPermissions()
            )
            .withListener(
                PermissionListener(
                    onSuccess,
                    onError
                )
            )
            .withErrorListener { onError(PermissionError.BLOCKED) }
            .check()
    }

    private fun getPermissions() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getPermissionsForAndroidQ()
        } else {
            getDefaultPermissions()
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getPermissionsForAndroidQ() = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE

    )

    private fun getDefaultPermissions() = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}
