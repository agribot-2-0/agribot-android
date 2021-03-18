package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.firestore.PropertyName
import eu.agribot.agribotapp.domain.model.cdb.EngineHealth

data class EngineHealthCDB(
    @get:PropertyName("error_code")
    @set:PropertyName("error_code")
    var errorCode: Int = -1,

    @get:PropertyName("error_name")
    @set:PropertyName("error_name")
    var errorName: String = ""
) : BaseCloudDatabaseModel<EngineHealth> {

    override fun toDomainModel() = EngineHealth(
        errorCode = this.errorCode,
        errorName = this.errorName
    )
}
