package eu.agribot.agribotapp.ui.fragment.weather

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentWeatherBinding
import kotlinx.android.synthetic.main.fragment_ttcontrol.horizontal_joystick
import kotlinx.android.synthetic.main.fragment_ttcontrol.vertical_joystick
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : BaseFragment<WeatherViewModel, FragmentWeatherBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_weather
    override val viewModel: WeatherViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentWeatherBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.prepareWeatherStationData()
    }

}
