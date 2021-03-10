package eu.agribot.agribotapp.ui.fragment.weather.forecast

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.databinding.ListItemWeatherForecastBinding
import kotlinx.android.synthetic.main.list_item_weather_forecast.view.*

class ForecastWeatherAdapter(fragment: Fragment) :
    RecyclerView.Adapter<ForecastWeatherViewHolder>() {
    var fragment = fragment
    var items = listOf<ForecastWeatherViewModel>()
        set(value) {
            DiffUtil.calculateDiff(
                ForecastWeatherDiffUtil(
                    items,
                    value
                )
            ).also {
                field = value
            }.dispatchUpdatesTo(this)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastWeatherViewHolder =
        ForecastWeatherViewHolder(
            ListItemWeatherForecastBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ForecastWeatherViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        Glide.with(fragment)
            .load(
                String.format(
                    holder.itemView.context
                        .getString(R.string.open_weather_image_url), item.imageNumber
                )
            )
            .error(R.drawable.ic_pointer)
            .override(200, 200)
            .centerCrop()
            .into(holder.itemView.forecastImage)
    }
}