package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.cdb.EngineHealth

data class EngineHealthCDB(
    val error_code: Int = -1,
    val error_name: String = ""
) : BaseCloudDatabaseModel<EngineHealth> {

    override fun toDomainModel() = EngineHealth(
        errorCode = this.error_code,
        errorName = this.error_name
    )
}
