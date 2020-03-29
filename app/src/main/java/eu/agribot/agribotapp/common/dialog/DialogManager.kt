package eu.agribot.agribotapp.common.dialog

/**
 * Interface used to show and handle dialog actions
 * @see AndroidDialogManager
 */
interface DialogManager {

    fun showExitDialog(onExitConfirmed: () -> Unit)
}
