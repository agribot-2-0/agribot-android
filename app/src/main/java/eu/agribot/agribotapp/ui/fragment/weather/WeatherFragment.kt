package eu.agribot.agribotapp.ui.fragment.weather

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentWeatherBinding
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateAdapter
import eu.agribot.agribotapp.ui.fragment.diagnosticdatarecycler.RobotStateViewModel
import eu.agribot.agribotapp.ui.fragment.weather.forecast.ForecastWeatherAdapter
import eu.agribot.agribotapp.ui.fragment.weather.forecast.ForecastWeatherViewModel
import kotlinx.android.synthetic.main.fragment_weather.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WeatherFragment : BaseFragment<WeatherViewModel, FragmentWeatherBinding>() {

    private val weatherRecyclerAdapter = RobotStateAdapter()
    override val layoutResourceId: Int = R.layout.fragment_weather
    override val viewModel: WeatherViewModel by viewModel { parametersOf(requireContext()) }
    private val forecastAdapter = ForecastWeatherAdapter(this)
    override fun bindViewVariables(viewBinding: FragmentWeatherBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.prepareWeatherStationData()
        setupRecyclers()
        observeViewModel()
        viewModel.getData()
        viewModel.currentWeatherImage.observe(this::setImage)
    }

    private fun setTemperatureChart() {
        temperatureChart.setBackgroundColor(Color.WHITE)
        temperatureChart.description.isEnabled = false
        temperatureChart.setTouchEnabled(true)
        temperatureChart.setDrawGridBackground(false)
        temperatureChart.isDragEnabled = true
        temperatureChart.setScaleEnabled(true)
        temperatureChart.setPinchZoom(true)
        temperatureChart.legend.textSize = resources.getDimension(R.dimen.text_size_small)
        temperatureChart.legend.form = Legend.LegendForm.CIRCLE
        temperatureChart.axisLeft.setDrawLabels(false)
        temperatureChart.axisRight.setDrawLabels(false)
    }

    private fun setCharts(isReady: Boolean) {
        if (isReady) {

            val values: List<Entry>? = viewModel.temperatureValuesChartList.value
            Log.e("xd", "values: " + values)
            try {
                val dataSettings: LineDataSet?
                if (temperatureChart.data != null &&
                    temperatureChart.data.dataSetCount > 0
                ) {
                    dataSettings = getDataSettings()

                    dataSettings?.values = values
                    if (dataSettings != null) {
                        //dataSettings.valueFormatter = WeatherValueFormatter()
                    }
                    dataSettings?.notifyDataSetChanged()
                } else {
                    dataSettings = LineDataSet(
                        values, ""
                    )
                    dataSettings.axisDependency = YAxis.AxisDependency.RIGHT
                    dataSettings.color = Color.RED
                    dataSettings.setCircleColor(Color.BLACK)
                    dataSettings.valueTextSize = resources.getDimension(R.dimen.text_size_small)

                    dataSettings.fillColor = Color.RED

                    dataSettings.setDrawFilled(true)
                    dataSettings.setDrawCircleHole(false)

                    val dataSets: ArrayList<ILineDataSet> = ArrayList()
                    dataSets.add(dataSettings)
                    temperatureChart.data = LineData(dataSets)
                }
            } catch (exception: Exception) {
                showToast(exception.toString())
            }
        }
    }


    private fun getDataSettings(): LineDataSet? {
        return temperatureChart.data.getDataSetByIndex(0) as LineDataSet?
    }


    private fun setImage(imageUrl: String) {
        Glide.with(this)
            .load(String.format(getString(R.string.open_weather_image_url), imageUrl))
            .error(R.drawable.ic_pointer)
            .override(200, 200)
            .centerCrop()
            .into(currentWeatherImage)
        setTemperatureChart()
    }

    private fun observeViewModel() {
        viewModel.weatherState.observe(this::addToWeatherList)
        viewModel.forecastWeatherItems.observe(this::addForecastWeatherItem)
        viewModel.isChartValuesReady.observe(this::setCharts)
    }

    private fun setupRecyclers() {
        forecastRecycler.adapter = forecastAdapter
        forecastRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        rightControlsRecycler.adapter = weatherRecyclerAdapter
        rightControlsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun addToWeatherList(weatherParam: List<RobotStateViewModel>) {
        weatherRecyclerAdapter.items = weatherParam
    }

    private fun addForecastWeatherItem(forecastWeatherItems: List<ForecastWeatherViewModel>) {
        forecastAdapter.items = forecastWeatherItems
    }
}
