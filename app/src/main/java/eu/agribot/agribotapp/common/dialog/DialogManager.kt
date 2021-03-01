package eu.agribot.agribotapp.common.dialog

/**
 * Interface used to show and handle dialog actions
 * @see AndroidDialogManager
 */
interface DialogManager {

    fun showExitDialog(onExitConfirmed: () -> Unit)

    fun showSignOutDialog(onSignOutConfirmed: () -> Unit)

    fun showRemoveAccountDialog(onDeleteAccountConfirmed: () -> Unit)

    fun showBluetoothDialog(onTurnOnBluetoothConfirmed: () -> Unit)

    fun showTaskSelectedDialog(
        taskName: String,
        onTaskSelectedConfirmed: (taskName: String) -> Unit,
        onNegativeButtonClicked: (refresh: Boolean) -> Unit

    )

    fun showDeleteTaskDialog(
        itemPosition: Int,
        onTaskToDeleteConfirmed: (itemPosition: Int) -> Unit,
        onTaskToDeleteAbort: () -> Unit
    )

    fun showDeletePathDialog(
        itemPosition: Int,
        onTaskToDeleteConfirmed: (itemPosition: Int) -> Unit,
        onTaskToDeleteAbort: () -> Unit
    )

    fun cancelTaskDialog(onCancelTaskConfirmed: () -> Unit)

    fun changeStreamUrlDialog(currentUrl: String, onSaveNewUrlConfirmed: (newUrl: String) -> Unit)

    fun showChangeRobotSerialNumberDialog(onSaveRobotSerialNumber: (newRobotSerialNumber: String) -> Unit)

    fun showNoFoundRfidTagDialog(onRepeatScanConfirmed: () -> Unit)
}
