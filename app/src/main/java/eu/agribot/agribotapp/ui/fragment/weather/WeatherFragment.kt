package eu.agribot.agribotapp.ui.fragment.weather

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentWeatherBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : BaseFragment<WeatherViewModel, FragmentWeatherBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_weather
    override val viewModel: WeatherViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentWeatherBinding) {
        viewBinding.viewModel = viewModel
    }
}
