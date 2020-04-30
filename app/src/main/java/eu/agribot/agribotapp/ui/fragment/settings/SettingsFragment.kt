package eu.agribot.agribotapp.ui.fragment.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_settings
    override val viewModel: SettingsViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentSettingsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showToastEvent.observe(this::showToast)
    }

    fun showToast(toastText: String) {
        Toast.makeText(activity, toastText, Toast.LENGTH_LONG).show()
    }
}
