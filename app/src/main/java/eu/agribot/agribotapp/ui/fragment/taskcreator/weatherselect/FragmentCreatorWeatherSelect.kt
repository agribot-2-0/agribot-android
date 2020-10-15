package eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskToolsNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentCreatorWeatherBinding
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_HUMIDITY_CONDITION
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_HUMIDITY_VALUE
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_PRESSURE_CONDITION
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_PRESSURE_VALUE
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_TEMPERATURE_CONDITION
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_TEMPERATURE_VALUE
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_WIND_CONDITION
import eu.agribot.agribotapp.ui.fragment.taskcreator.weatherselect.PickerNumber.PICKER_WIND_VALUE
import kotlinx.android.synthetic.main.fragment_creator_weather.humidity_condition
import kotlinx.android.synthetic.main.fragment_creator_weather.humidity_value
import kotlinx.android.synthetic.main.fragment_creator_weather.pressure_condition
import kotlinx.android.synthetic.main.fragment_creator_weather.pressure_value
import kotlinx.android.synthetic.main.fragment_creator_weather.temperature_condition
import kotlinx.android.synthetic.main.fragment_creator_weather.temperature_value
import kotlinx.android.synthetic.main.fragment_creator_weather.wind_condition
import kotlinx.android.synthetic.main.fragment_creator_weather.wind_value
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCreatorWeatherSelect :
    BaseFragment<CreatorWeatherSelectViewModel, FragmentCreatorWeatherBinding>() {

    var arrayPicker = arrayOf("<", ">", "=")

    override val layoutResourceId: Int = R.layout.fragment_creator_weather
    override val viewModel: CreatorWeatherSelectViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentCreatorWeatherBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPickersConditions()
        setPickersValue()
        onPickerValuesChanged()
        viewModel.checkIsWeatherSelected()
        viewModel.getRobotDataValue()
    }

    private fun setPickersConditions() {
        temperature_condition.minValue = 0
        temperature_condition.maxValue = arrayPicker.size - 1
        temperature_condition.displayedValues = arrayPicker

        humidity_condition.minValue = 0
        humidity_condition.maxValue = arrayPicker.size - 1
        humidity_condition.displayedValues = arrayPicker

        wind_condition.minValue = 0
        wind_condition.maxValue = arrayPicker.size - 1
        wind_condition.displayedValues = arrayPicker

        pressure_condition.minValue = 0
        pressure_condition.maxValue = arrayPicker.size - 1
        pressure_condition.displayedValues = arrayPicker
    }

    private fun setPickersValue() {
        temperature_value.minValue = 0
        temperature_value.maxValue = 70
        temperature_value.value = 20

        humidity_value.minValue = 0
        humidity_value.maxValue = 100
        humidity_value.value = 50

        wind_value.minValue = 0
        wind_value.maxValue = 150
        wind_value.value = 10

        pressure_value.minValue = 950
        pressure_value.maxValue = 1050
        pressure_value.value = 1013
    }

    private fun onPickerValuesChanged() {
        temperature_condition.setOnValueChangedListener { _, _, newVal ->
            viewModel.onWeatherParametrsChanged(
                PICKER_TEMPERATURE_CONDITION,
                newVal
            )
        }
        temperature_value.setOnValueChangedListener { _, _, newVal ->
            viewModel.onWeatherParametrsChanged(
                PICKER_TEMPERATURE_VALUE,
                newVal
            )
        }
        humidity_condition.setOnValueChangedListener { _, _, newVal ->
            viewModel.onWeatherParametrsChanged(
                PICKER_HUMIDITY_CONDITION,
                newVal
            )
        }
        humidity_value.setOnValueChangedListener { _, _, newVal ->
            viewModel.onWeatherParametrsChanged(
                PICKER_HUMIDITY_VALUE,
                newVal
            )
        }
        wind_value.setOnValueChangedListener { _, _, newVal ->
            viewModel.onWeatherParametrsChanged(
                PICKER_WIND_VALUE,
                newVal
            )
        }
        wind_condition.setOnValueChangedListener { _, _, newVal ->
            viewModel.onWeatherParametrsChanged(
                PICKER_WIND_CONDITION,
                newVal
            )
        }
        pressure_value.setOnValueChangedListener { _, _, newVal ->
            viewModel.onWeatherParametrsChanged(
                PICKER_PRESSURE_VALUE,
                newVal
            )
        }
        pressure_condition.setOnValueChangedListener { _, _, newVal ->
            viewModel.onWeatherParametrsChanged(
                PICKER_PRESSURE_CONDITION,
                newVal
            )
        }
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is CreatorTaskToolsNavigationEvent -> navigateToCreatorTaskToolFragment()
        else -> Unit
    }

    private fun navigateToCreatorTaskToolFragment() {
        navigationController?.navigate(R.id.action_taskWeatherCreatorFragment_to_taskToolCreatorFragment)
    }
}
