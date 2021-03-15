package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.gson.annotations.SerializedName
import eu.agribot.agribotapp.domain.model.cdb.UserInfo

data class UserInfoCDB(
    @SerializedName("robot_name")
    val robotName: String = "",

    @SerializedName("user_name")
    val userName: String = ""

) : BaseCloudDatabaseModel<UserInfo> {

    override fun toDomainModel() = UserInfo(
        robotName = this.robotName,
        userName = this.userName
    )
}
