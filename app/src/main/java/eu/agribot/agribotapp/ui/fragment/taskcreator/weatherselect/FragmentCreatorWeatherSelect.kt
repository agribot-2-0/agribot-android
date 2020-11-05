package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskToolsNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorWeatherBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.WeatherSettingsListItemAdapter
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.WeatherSettingsListItemViewModel
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.recycler.weatherattributes.WeatherAttribute
import kotlinx.android.synthetic.main.fragment_creator_weather.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FragmentCreatorWeatherSelect :
    BaseFragment<CreatorWeatherSelectViewModel, FragmentCreatorWeatherBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_creator_weather
    override val viewModel: CreatorWeatherSelectViewModel by viewModel { parametersOf(requireContext()) }
    private val recyclerAdapter = WeatherSettingsListItemAdapter(
        this::onWeatherAttributeChanged
    )

    override fun bindViewVariables(viewBinding: FragmentCreatorWeatherBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.weatherAttributeList.observe(this::addAvaliableWeatherAttribute)
        setupRecyclerView()
        viewModel.prepareWeatherAttributeListItem()
    }

    private fun onWeatherAttributeChanged(weatherAttribute: WeatherAttribute) {
        viewModel.onWeatherAttributeChanged(weatherAttribute)
    }

    private fun setupRecyclerView() {
        weather_settings_recycler.adapter = recyclerAdapter
        weather_settings_recycler.layoutManager = GridLayoutManager(context, 2)
    }

    private fun addAvaliableWeatherAttribute(weatherAttribute: List<WeatherSettingsListItemViewModel>) {
        recyclerAdapter.items = weatherAttribute
        recyclerAdapter.updateList(weatherAttribute)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is CreatorTaskToolsNavigationEvent -> navigateToCreatorTaskToolFragment()
        else -> Unit
    }

    private fun navigateToCreatorTaskToolFragment() {
        navigationController?.navigate(R.id.action_taskWeatherCreatorFragment_to_taskToolCreatorFragment)
    }
}
