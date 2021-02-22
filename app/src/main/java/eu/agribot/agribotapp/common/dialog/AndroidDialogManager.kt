package eu.agribot.agribotapp.common.dialog

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.resources.ResourcesProvider

class AndroidDialogManager(
    private val context: Context,
    private val resourcesProvider: ResourcesProvider
) : DialogManager {

    override fun showExitDialog(onExitConfirmed: () -> Unit) {
        MaterialDialog(context).show {
            title(R.string.dialog_exit_title)
            message(R.string.dialog_exit_message)
            positiveButton(R.string.exit) { onExitConfirmed() }
            negativeButton(R.string.cancel)
        }
    }

    override fun showSignOutDialog(onSignOutConfirmed: () -> Unit) {
        MaterialDialog(context).show {
            title(R.string.dialog_signout_tittle)
            message(R.string.dialog_signout_message)
            positiveButton(R.string.signout) { onSignOutConfirmed() }
            negativeButton(R.string.cancel)
        }
    }

    override fun showRemoveAccountDialog(onDeleteAccountConfirmed: () -> Unit) {
        MaterialDialog(context).show {
            title(R.string.dialog_remove_account_title)
            message(R.string.dialog_remove_account_message)
            positiveButton(R.string.delete_account) { onDeleteAccountConfirmed() }
            negativeButton(R.string.cancel)
        }
    }

    override fun showBluetoothDialog(
        onTurnOnBluetoothConfirmed: () -> Unit
    ) {
        MaterialDialog(context).show {
            title(R.string.dialog_bluetooth_title)
            message(R.string.dialog_bluetooth_message)
            positiveButton(R.string.dialog_bluetooth_positive_message) { onTurnOnBluetoothConfirmed() }
            negativeButton(R.string.cancel)
        }
    }

    override fun showTaskSelectedDialog(
        taskName: String,
        onTaskSelectedConfirmed: (taskName: String) -> Unit
    ) {
        MaterialDialog(context).show {
            title(R.string.dialog_task_select_tittle)
            message(R.string.dialog_task_select_message)
            positiveButton(R.string.dialog_task_select_confirm) { onTaskSelectedConfirmed(taskName) }
            negativeButton(R.string.cancel)
        }
    }

    override fun showDeleteTaskDialog(
        itemPosition: Int,
        onTaskToDeleteConfirmed: (itemPosition: Int) -> Unit,
        onTaskToDeleteAbort: () -> Unit

    ) {
        MaterialDialog(context).show {
            title(R.string.dialog_task_delete_tittle)
            message(R.string.dialog_task_delete_message)
            positiveButton(R.string.dialog_task_delete_confirm) {
                onTaskToDeleteConfirmed(
                    itemPosition
                )
            }
            negativeButton(R.string.cancel) {
                onTaskToDeleteAbort.invoke()
            }
        }
    }

    override fun showDeletePathDialog(
        itemPosition: Int,
        onTaskToDeleteConfirmed: (itemPosition: Int) -> Unit,
        onTaskToDeleteAbort: () -> Unit

    ) {
        MaterialDialog(context)
            .show {
                title(R.string.dialog_path_delete_tittle)
                message(R.string.dialog_path_delete_message)
                positiveButton(R.string.dialog_path_delete_confirm) {
                    onTaskToDeleteConfirmed(
                        itemPosition
                    )
                }
                negativeButton(R.string.cancel) {
                    onTaskToDeleteAbort.invoke()
                }
            }
    }

    override fun cancelTaskDialog(onCancelTaskConfirmed: () -> Unit) {
        MaterialDialog(context).show {
            title(R.string.dialog_task_cancel_tittle)
            message(R.string.dialog_task_cancel_message)
            positiveButton(R.string.dialog_task_select_confirm) { onCancelTaskConfirmed() }
            negativeButton(R.string.cancel)
        }
    }

    override fun changeStreamUrlDialog(
        currentUrl: String,
        onSaveNewUrlConfirmed: (newUrl: String) -> Unit
    ) {
        MaterialDialog(context).show {
            title(R.string.change_video_url)
            message(R.string.actual_url, currentUrl)
            input(waitForPositiveButton = true) { _, text ->
                onSaveNewUrlConfirmed(text.toString())
            }
            positiveButton(R.string.done)
        }
    }

    override fun showChangeRobotSerialNumberDialog(onSaveRobotSerialNumber: (newUrl: String) -> Unit) {
        MaterialDialog(context).show {
            title(R.string.robot_serial_number_dialog_title)
            message(R.string.type_robot_serial_number_dialog_message)
            input(waitForPositiveButton = true) { _, text ->
                onSaveRobotSerialNumber(text.toString())
            }
            positiveButton(R.string.done)
        }
    }

    override fun showNoFoundRfidTagDialog(onRepeatScanConfirmed: () -> Unit) {
        MaterialDialog(context).show {
            title(R.string.dialog_no_found_tags_tittle)
            message(R.string.dialog_no_found_tags_message)
            positiveButton(R.string.dialog_no_found_tags_confirm) { onRepeatScanConfirmed() }
            negativeButton(R.string.cancel)
        }
    }
}
