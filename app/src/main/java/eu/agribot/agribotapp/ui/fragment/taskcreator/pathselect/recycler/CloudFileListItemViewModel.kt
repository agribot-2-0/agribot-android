package eu.agribot.agribotapp.ui.fragment.taskcreator.pathselect.recycler

import eu.agribot.agribotapp.base.BaseViewModel

data class CloudFileListItemViewModel(
    val fileName: String,
    val isFileSelected : Boolean
) : BaseViewModel()
