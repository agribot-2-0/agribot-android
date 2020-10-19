package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskToolsNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorWeatherBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.WeatherSettingsListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.WeatherSettingsListItemViewModel
import kotlinx.android.synthetic.main.fragment_creator_weather.weather_settings_recycler
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class FragmentCreatorWeatherSelect :
    BaseFragment<CreatorWeatherSelectViewModel, FragmentCreatorWeatherBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_creator_weather
    override val viewModel: CreatorWeatherSelectViewModel by viewModel { parametersOf(requireContext()) }
    val recyclerAdapter = WeatherSettingsListItemAdapter(
        this::onConditionPickerChanged,
        this::onValuePickerChanged,
        this::onItemSwitchParametrChange
    )

    override fun bindViewVariables(viewBinding: FragmentCreatorWeatherBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRobotDataValue()
        viewModel.weatherAttributeList.observe(this::addAvaliableWeatherAttribute)
        setupRecyclerView()
        viewModel.backButtonEvent.observe(this::backToPreviousFragment)
    }

    private fun backToPreviousFragment(isBackAction: Boolean) {
        if (isBackAction)
        requireActivity().onBackPressed()
    }

    private fun onConditionPickerChanged(condition: String, itemPosition: Int) {
        viewModel.onConditionChanged(condition, itemPosition)
    }

    private fun onValuePickerChanged(value: Int, itemPosition: Int) {
        viewModel.onValueChanged(value, itemPosition)
    }

    private fun onItemSwitchParametrChange(position: Int, isActivated: Boolean) {
        viewModel.onSwitchParametrChange(position, isActivated)
    }

    private fun setupRecyclerView() {
        weather_settings_recycler.adapter = recyclerAdapter
        weather_settings_recycler.layoutManager = GridLayoutManager(context, 2)

    }

    private fun addAvaliableWeatherAttribute(weatherAttribute: List<WeatherSettingsListItemViewModel>) {
        Log.e("xd", "items: " + weatherAttribute)
        recyclerAdapter.items = weatherAttribute
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is CreatorTaskToolsNavigationEvent -> navigateToCreatorTaskToolFragment()
        else -> Unit
    }

    private fun navigateToCreatorTaskToolFragment() {
        navigationController?.navigate(R.id.action_taskWeatherCreatorFragment_to_taskToolCreatorFragment)
    }
}
