package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.cdb.UserInfo

data class UserInfoCDB(
    val robot_name: String = "",
    val user_name: String = ""

) : BaseCloudDatabaseModel<UserInfo> {

    override fun toDomainModel() = UserInfo(
        robotName = this.robot_name,
        userName = this.user_name
    )
}
