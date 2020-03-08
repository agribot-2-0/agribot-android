package eu.agribot.agribotapp.common.dialog

interface DialogManager {

    fun showExitDialog(onExitConfirmed: () -> Unit)
}
