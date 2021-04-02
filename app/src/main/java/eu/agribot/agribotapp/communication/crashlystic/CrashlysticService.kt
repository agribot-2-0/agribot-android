package eu.agribot.agribotapp.communication.crashlystic

interface CrashlysticService {
    fun recordException(excrptionModel: RegisterExceptionModel)

    data class RegisterExceptionModel(
        val message: String,
        val exception: Throwable
    )
}
