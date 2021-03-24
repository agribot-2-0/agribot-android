package eu.agribot.agribotapp.ui.fragment.weather.forecast.chart

import android.text.format.DateFormat
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.taskdetails.TimePatterns
import java.util.Calendar
import java.util.Locale

class TimestampValueFormatter : IndexAxisValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        // value is time stamp from epochTime
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = getEpochTimeInMillis(value)
        if (calendar.get(Calendar.MINUTE) >= ONE_HALF_HOUR) {
            calendar.add(Calendar.HOUR, 1)
            calendar.set(Calendar.MINUTE, 0)
        } else {
            calendar.roll(Calendar.HOUR, 1)
            calendar.set(Calendar.MINUTE, 0)
        }
        return DateFormat.format(TimePatterns.TIME.pattern, calendar).toString()
    }

    private fun getEpochTimeInMillis(time: Float): Long {
        return (time * DIFFERENCE_BETWEEN_NANOSEC_TO_MILLIS).toLong()
    }

    companion object {
        const val ONE_HALF_HOUR = 30

        const val DIFFERENCE_BETWEEN_NANOSEC_TO_MILLIS = 1000L
    }
}
