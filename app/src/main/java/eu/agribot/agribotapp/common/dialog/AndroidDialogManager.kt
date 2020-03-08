package eu.agribot.agribotapp.common.dialog

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
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
}
