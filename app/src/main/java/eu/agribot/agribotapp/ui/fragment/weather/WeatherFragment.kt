package eu.agribot.agribotapp.ui.fragment.weather

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentWeatherBinding
import eu.agribot.agribotapp.ui.fragment.weather.forecast.ForecastWeatherAdapter
import eu.agribot.agribotapp.ui.fragment.weather.forecast.ForecastWeatherViewModel
import eu.agribot.agribotapp.ui.fragment.weather.forecast.chart.ChartWeatherData
import eu.agribot.agribotapp.ui.fragment.weather.forecast.chart.TimestampValueFormatter
import kotlinx.android.synthetic.main.fragment_weather.temperatureChart
import kotlinx.android.synthetic.main.fragment_weather.currentWeatherImage
import kotlinx.android.synthetic.main.fragment_weather.forecastRecycler
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WeatherFragment : BaseFragment<WeatherViewModel, FragmentWeatherBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_weather
    override val viewModel: WeatherViewModel by viewModel { parametersOf(requireContext()) }
    private val forecastAdapter = ForecastWeatherAdapter(this)
    override fun bindViewVariables(viewBinding: FragmentWeatherBinding) {
        viewBinding.viewModel = viewModel
    }

    private var values: List<Entry> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.prepareWeatherStationData()
        setupRecyclers()
        observeViewModel()
        viewModel.getForecastData()
        temperatureChart.setTouchEnabled(true)
        temperatureChart.setPinchZoom(true)
        initializeChart()
    }

    private fun setForecastWeatherImage(imageUrl: String) {
        Glide.with(this)
            .load(String.format(getString(R.string.open_weather_image_url), imageUrl))
            .error(R.drawable.ic_pointer)
            .override(FORECAST_WEATHER_IMAGE_SIZE, FORECAST_WEATHER_IMAGE_SIZE)
            .centerCrop()
            .into(currentWeatherImage)
    }

    private fun observeViewModel() {
        viewModel.forecastWeatherItems.observe(this::addForecastWeatherItem)
        viewModel.chartWeatherData.observe(this::appendPlotData)
        viewModel.currentWeatherImage.observe(this::setForecastWeatherImage)
    }

    private fun setupRecyclers() {
        forecastRecycler.adapter = forecastAdapter
        forecastRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun addForecastWeatherItem(forecastWeatherItems: List<ForecastWeatherViewModel>) {
        forecastAdapter.items = forecastWeatherItems
    }

    private fun initializeChart() {
        val chart = temperatureChart
        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setDrawGridBackground(false)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.xAxis.valueFormatter = TimestampValueFormatter()
        chart.xAxis.setLabelCount(TWO_HOURS_COUNT, true)
        chart.setPinchZoom(true)
    }

    private fun appendPlotData(plotDataList: List<ChartWeatherData>) {
        temperatureChart.invalidate()
        if (plotDataList.isNotEmpty()) {
            try {
                values =
                    plotDataList.map {
                        Entry(
                            it.timestamp,
                            it.temperatureValue
                        )
                    }
            } catch (exception: Exception) {
                showToast(exception.toString())
            }
            try {
                val temperatureDataSet: LineDataSet?
                if (temperatureChart.data != null &&
                    temperatureChart.data.dataSetCount > 0
                ) {
                    temperatureDataSet = temperatureChart.data.getDataSetByIndex(0) as LineDataSet?
                    temperatureDataSet?.values = values
                    temperatureDataSet?.notifyDataSetChanged()
                    temperatureDataSet?.setDrawVerticalHighlightIndicator(false)
                    temperatureChart.data.notifyDataChanged()
                    temperatureChart.notifyDataSetChanged()
                } else {
                    temperatureDataSet = LineDataSet(values, getString(R.string.temperature))
                    temperatureDataSet.setDrawIcons(false)
                    temperatureDataSet.setDrawVerticalHighlightIndicator(false)
                    temperatureDataSet.color = Color.RED
                    temperatureDataSet.setCircleColor(Color.BLACK)
                    temperatureDataSet.circleRadius = 4f
                    temperatureDataSet.setDrawCircleHole(false)

                    temperatureDataSet.valueTextSize = 10F

                    temperatureDataSet.setDrawFilled(true)
                    val dataSets: ArrayList<ILineDataSet> = ArrayList()
                    dataSets.add(temperatureDataSet) // add the data sets
                    val data = LineData(dataSets)
                    temperatureChart.data = data
                }
            } catch (exception: Exception) {
                showToast(exception.toString())
            }
        }
    }

    companion object {
        const val FORECAST_WEATHER_IMAGE_SIZE = 200
        const val TWO_HOURS_COUNT = 12
    }
}
