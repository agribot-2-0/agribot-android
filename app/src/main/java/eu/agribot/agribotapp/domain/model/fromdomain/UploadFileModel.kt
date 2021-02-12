package eu.agribot.agribotapp.domain.model.fromdomain

import eu.agribot.agribotapp.communication.database.clouddatabase.model.UploadFileModelCDB

data class UploadFileModel(
    val filePath: String,
    val fileName: String,
    val fileSource: String
) : BaseDomainDataModel<UploadFileModelCDB> {
    override fun toCommunicationModel() = UploadFileModelCDB(
        filePath = this.filePath,
        fileNAme = this.fileName,
        fileSource = this.fileSource
    )
}
