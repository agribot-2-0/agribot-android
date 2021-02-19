package eu.agribot.agribotapp.communication.database.clouddatabase.model

import eu.agribot.agribotapp.domain.model.todomain.UserRole

data class UserRoleCDB(
    val role: String = ""
) : BaseCloudDatabaseModel<UserRole> {
    override fun toDomainModel() = UserRole(
        role = this.role
    )
}
