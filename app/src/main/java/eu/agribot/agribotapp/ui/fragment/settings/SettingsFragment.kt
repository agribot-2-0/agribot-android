package eu.agribot.agribotapp.ui.fragment.settings

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentSettingsBinding
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_settings
    override val viewModel: SettingsViewModel by viewModel { parametersOf(requireContext()) }

    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: FragmentSettingsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.toastEvent.observe(this::showToast)
        viewModel.showSignoutDialogEvent.observe(this::showSignOutDialog)
        viewModel.showDeleteAccountDialogEvent.observe(this::showDeleteAccountDialog)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is WelcomeActivityNavigationEvent -> handleWelcomeActivityNavigationEvent()
        is BluetoothConnectionNavigationEvent -> handleBluetoothConnectionNavigationEvent()
        else -> Unit
    }

    private fun handleBluetoothConnectionNavigationEvent() {
        navigationController?.navigate(R.id.action_settingsFragment_to_bluetoothConnectionFragment)
    }

    private fun handleWelcomeActivityNavigationEvent() {
        startActivity(WelcomeActivity.getStartingIntent(requireContext()))
    }

    private fun showSignOutDialog(logOut: Boolean) {
        dialogManager.showSignOutDialog(viewModel::logOut)
    }

    private fun showDeleteAccountDialog(deleteAccountDecision: Boolean) {
        if (deleteAccountDecision) {
            dialogManager.showRemoveAccountDialog(viewModel::removeUser)
        }
    }
}
