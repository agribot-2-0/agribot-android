package eu.agribot.agribotapp.ui.fragment.taskcreator.timeselect

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskWeatherNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorTimeSelectBinding
import kotlinx.android.synthetic.main.fragment_creator_time_select.date_picker
import kotlinx.android.synthetic.main.fragment_creator_time_select.time_activator
import kotlinx.android.synthetic.main.fragment_creator_time_select.time_picker
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCreatorTimeSelect :
    BaseFragment<CreatorTimeSelectViewModel, FragmentCreatorTimeSelectBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_creator_time_select
    override val viewModel: CreatorTimeSelectViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentCreatorTimeSelectBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is CreatorTaskWeatherNavigationEvent -> navigateToCreatorTaskWeatherFragment()
        else -> Unit
    }

    private fun navigateToCreatorTaskWeatherFragment() {
        navigationController?.navigate(R.id.action_taskTimeCreatorFragment_to_taskWeatherCreatorFragment)
    }

    private fun backToPreviousFragment(isBackAction: Boolean) {
        if (isBackAction)
            requireActivity().onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkIsDateSelected()
        viewModel.backButtonEvent.observe(this::backToPreviousFragment)
        time_picker.setIs24HourView(true)
        time_picker.setOnTimeChangedListener { view, hourOfDay, minute ->
            viewModel.onTimeChange(hourOfDay, minute)
        }

        date_picker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            viewModel.setCallendarSettings(dayOfMonth, monthOfYear, year)
        }

        time_activator.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTimeActivator(isChecked)
        }
    }
}
