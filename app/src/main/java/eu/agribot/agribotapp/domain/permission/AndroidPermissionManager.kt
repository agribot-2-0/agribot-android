package eu.agribot.agribotapp.domain.permission

import android.Manifest
import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class AndroidPermissionManager(val context: Context) : PermissionManager {
    private val permissions = listOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun requestLocationPermission(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        Dexter.withContext(context)
            .withPermissions(permissions).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    onSuccess.invoke()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?, token: PermissionToken?
                ) {
                    onError.invoke()
                }
            }).check()
    }
}


