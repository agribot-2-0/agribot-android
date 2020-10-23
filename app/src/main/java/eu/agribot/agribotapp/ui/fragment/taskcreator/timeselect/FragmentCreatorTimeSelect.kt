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
import kotlinx.android.synthetic.main.fragment_creator_time_select.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

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
            navigationController?.navigate(R.id.action_taskCreatorTaskTimeFragment_to_taskCreatorTaskPathFragment)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPreSetDate()
        viewModel.backButtonEvent.observe(this::backToPreviousFragment)
        viewModel.date.observe(this::setDatePicker)
        time_picker.setIs24HourView(true)

        time_picker.setOnTimeChangedListener { _, hourOfDay, minute ->
            viewModel.onTimeChange(hourOfDay, minute)
        }

        date_picker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            viewModel.onDateChange(dayOfMonth, monthOfYear, year)
        }

        time_activator.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTimeActivator(isChecked)
        }
    }

    private fun setDatePicker(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        date_picker.updateDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        time_picker.hour = calendar.get(Calendar.HOUR)
        time_picker.minute = calendar.get(Calendar.MINUTE)
    }
}
