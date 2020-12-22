package eu.agribot.agribotapp.ui.fragment.taskcreator.mainscreen.recycler

import eu.agribot.agribotapp.base.BaseViewModel

data class TaskListItemViewModel(
    val taskName: String,
    val pathName: String
) : BaseViewModel()
