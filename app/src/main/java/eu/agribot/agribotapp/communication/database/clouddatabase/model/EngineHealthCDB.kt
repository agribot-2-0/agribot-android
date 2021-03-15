package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.domain.model.cdb.EngineHealth

data class EngineHealthCDB(

    @SerializedName("error_code")
    val errorCode: Int = -1,

    @SerializedName("error_name")
    val errorName: String = ""
) : BaseCloudDatabaseModel<EngineHealth> {

    override fun toDomainModel() = EngineHealth(
        errorCode = this.errorCode,
        errorName = this.errorName
    )
}
