package eu.agribot.agribotapp.permissions

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener.Builder.withContext


class AndroidPermissionManager(val context: Context) : PermissionManager {
    override fun requestLocationPermission(
        onSuccess: () -> Unit,
        onError: (PermissionError) -> Unit
    ) {
        TODO("Not yet implemented")
    }

}