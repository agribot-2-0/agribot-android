package eu.agribot.agribotapp.ui.fragment.weather

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentWeatherBinding
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateAdapter
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import kotlinx.android.synthetic.main.fragment_weather.rightControlsRecycler
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : BaseFragment<WeatherViewModel, FragmentWeatherBinding>() {

    private val weatherRecyclerAdapter = RobotStateAdapter()
    override val layoutResourceId: Int = R.layout.fragment_weather
    override val viewModel: WeatherViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentWeatherBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.prepareWeatherStationData()
        setupRecycler()
        viewModel.weatherState.observe(this:: addToWeatherList)
    }

    private fun setupRecycler() {
        rightControlsRecycler.adapter = weatherRecyclerAdapter
        rightControlsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun addToWeatherList(weatherParam: List<RobotStateViewModel>) {
        weatherRecyclerAdapter.items = weatherParam
    }
}
