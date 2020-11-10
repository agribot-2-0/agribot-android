package eu.agribot.agribotapp.domain.model.cdb

import eu.agribot.agribotapp.communication.database.clouddatabase.model.UserInfoCDB

data class UserInfo(
    val robotName: String,
    val userName: String

) : BaseCloudDatabaseModel<UserInfoCDB> {

    override fun toCommunicationModel() = UserInfoCDB(
        robot_name = this.robotName,
        user_name = this.userName
    )
}
