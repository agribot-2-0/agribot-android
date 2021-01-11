package eu.agribot.agribotapp.ui.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.BluetoothConnectionNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.WelcomeActivityNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentSettingsBinding
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeActivity
import eu.agribot.agribotapp.ui.fragment.settings.recycler.settingsaction.SettingsActionAdapter
import eu.agribot.agribotapp.ui.fragment.settings.recycler.settingsaction.SettingsActionViewModel
import eu.agribot.agribotapp.ui.fragment.settings.recycler.settingslist.SettingsListAdapter
import eu.agribot.agribotapp.ui.fragment.settings.recycler.settingslist.SettingsListViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_settings
    override val viewModel: SettingsViewModel by viewModel { parametersOf(requireContext()) }

    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }
    private val settingsListAdapter = SettingsListAdapter(this::onSettingsItemCLicked)
    private val settingsActionAdapter = SettingsActionAdapter(this::onSettingsActionCLicked)

    override fun bindViewVariables(viewBinding: FragmentSettingsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        viewModel.prepareSettingsItem()
        viewModel.prepareSettingsAction()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.settingsList.observe(this::addToSettingsList)
        viewModel.settingsActions.observe(this::addToSettingsAction)
        viewModel.toastEvent.observe(this::showToast)
        viewModel.showDeleteAccountDialogEvent.observe(this::showDeleteAccountDialog)
        viewModel.showSignoutDialogEvent.observe(this::showSignOutDialog)
    }

    private fun onSettingsItemCLicked(itemID: Int) {
        viewModel.onSettingsItemSelected(itemID)
    }

    private fun onSettingsActionCLicked(actionID: Int) {
        viewModel.onSettingsActionSelected(actionID)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is WelcomeActivityNavigationEvent -> handleWelcomeActivityNavigationEvent()
        is BluetoothConnectionNavigationEvent -> handleBluetoothConnectionNavigationEvent()
        else -> Unit
    }

    private fun setupRecyclers() {
        settingsListRecycler.adapter = settingsListAdapter
        settingsListRecycler.layoutManager = LinearLayoutManager(requireActivity())

        settingsActionsRecycler.adapter = settingsActionAdapter
        settingsActionsRecycler.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun addToSettingsList(settingsItem: List<SettingsListViewModel>) {
        settingsListAdapter.items = settingsItem
    }

    private fun addToSettingsAction(settingsAction: List<SettingsActionViewModel>) {
        settingsActionAdapter.items = settingsAction
    }

    private fun handleBluetoothConnectionNavigationEvent() {
        navigationController?.navigate(R.id.action_settingsFragment_to_bluetoothConnectionFragment)
    }

    private fun handleWelcomeActivityNavigationEvent() {
        startActivity(WelcomeActivity.getStartingIntent(requireContext()))
    }

    private fun showSignOutDialog(isSignoutDecision: Boolean) {
        if (isSignoutDecision) {
            dialogManager.showSignOutDialog(viewModel::logOut)
        }
    }

    private fun showDeleteAccountDialog(deleteAccountDecision: Boolean) {
        if (deleteAccountDecision) {
            dialogManager.showRemoveAccountDialog(viewModel::removeUser)
        }
    }
}
