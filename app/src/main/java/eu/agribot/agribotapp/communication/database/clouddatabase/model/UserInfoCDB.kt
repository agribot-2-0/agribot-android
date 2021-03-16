package eu.agribot.agribotapp.communication.database.clouddatabase.model

import com.google.firebase.firestore.PropertyName
import eu.agribot.agribotapp.domain.model.cdb.UserInfo

data class UserInfoCDB(
    @get:PropertyName("robot_name")
    @set:PropertyName("robot_name")
    var robotName: String = "",

    @get:PropertyName("user_name")
    @set:PropertyName("user_name")
    var userName: String = ""

) : BaseCloudDatabaseModel<UserInfo> {

    override fun toDomainModel() = UserInfo(
        robotName = this.robotName,
        userName = this.userName
    )
}
