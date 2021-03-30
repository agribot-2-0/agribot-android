package eu.agribot.agribotapp.communication.crashlystic

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider
import eu.agribot.agribotapp.domain.service.AuthenticationService

class AndroidCrashlysticService(
    val resourcesProvider: ResourcesProvider
) :
    CrashlysticService {

    override fun recordException(excrptionModel: CrashlysticService.RegisterExceptionModel) {
        FirebaseCrashlytics.getInstance().log(
            String.format(
                resourcesProvider.getString(R.string.crashlystic_log),
                FirebaseAuth.getInstance().currentUser?.uid.toString(),
                excrptionModel.message,
                excrptionModel.exception
            )
        )
        FirebaseCrashlytics.getInstance().recordException(excrptionModel.exception)
    }
}
