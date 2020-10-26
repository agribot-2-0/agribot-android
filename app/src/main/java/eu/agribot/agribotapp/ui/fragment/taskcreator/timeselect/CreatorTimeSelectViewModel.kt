package eu.agribot.agribotapp.ui.fragment.taskcreator.timeselect

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import eu.agribot.agribotapp.base.BaseViewModel
import eu.agribot.agribotapp.common.navigation.events.CreatorTaskWeatherNavigationEvent
import eu.agribot.agribotapp.domain.service.cloud.TaskCreatorDataManager
import eu.agribot.agribotapp.ui.fragment.taskcreator.TaskDetailsElements
import java.util.*

class CreatorTimeSelectViewModel(private val taskCreatorDataManager: TaskCreatorDataManager) :
    BaseViewModel() {

    fun onNextButtonClicked() {
        navigationEvent.postValue(
            CreatorTaskWeatherNavigationEvent()
        )
    }

    private val yearData: MutableLiveData<Int> = MutableLiveData()
    private val monthData: MutableLiveData<Int> = MutableLiveData()
    private val dayOfMonthData: MutableLiveData<Int> = MutableLiveData()

    private val minuteData: MutableLiveData<Int> = MutableLiveData()
    private val hourData: MutableLiveData<Int> = MutableLiveData()
    val date: MutableLiveData<Date> = MutableLiveData()
    val isTimePreset: MutableLiveData<Boolean> = MutableLiveData()
    private val miliToSecondDivide = 1000

    fun getPreSetDate() {
        if (!taskCreatorDataManager.getTimeEstimated() && taskCreatorDataManager.getTimeStamp() != null) {
            val date: Date = taskCreatorDataManager.getTimeStamp()!!.toDate()
            this.date.value = date
            isTimePreset.value = true
        } else {
            isTimePreset.value = false
        }
    }

    fun setTimeActivator(isChecked: Boolean) {
        taskCreatorDataManager.onUpdateTimestampEstimated(!isChecked)
    }


    fun onDateChange(day: Int, month: Int, year: Int) {
        if (minuteData.value == null && hourData.value == null) {
            minuteData.value = GregorianCalendar.MINUTE
            hourData.value = GregorianCalendar.HOUR
            taskCreatorDataManager.onUpdateTimestampEstimated(true)
        }
        yearData.value = year
        monthData.value = month
        dayOfMonthData.value = day

        val gregorianCalendar =
            GregorianCalendar(
                yearData.value!!,
                monthData.value!!,
                dayOfMonthData.value!!,
                hourData.value!!,
                minuteData.value!!
            )
        gregorianCalendar.timeZone =
            TimeZone.getTimeZone(TaskDetailsElements.TIMEZONE.detailsElement)
        val calendar = (gregorianCalendar.timeInMillis) / miliToSecondDivide
        val timestamp = Timestamp(calendar, 0)
        taskCreatorDataManager.onUpdateTimestamp(timestamp)
        taskCreatorDataManager.onUpdateTimestampEstimated(false)
    }

    fun onTimeChange(hour: Int, minute: Int) {
        if (yearData.value == null || monthData.value == null || dayOfMonthData.value == null) {
            yearData.value = GregorianCalendar.YEAR
            monthData.value = GregorianCalendar.MONTH
            dayOfMonthData.value = GregorianCalendar.DAY_OF_MONTH
            taskCreatorDataManager.onUpdateTimestampEstimated(true)
        }
        hourData.value = hour
        minuteData.value = minute
        val tempCalendar = Calendar.getInstance()
        tempCalendar.get(Calendar.YEAR)
        tempCalendar.get(Calendar.MONTH)
        tempCalendar.get((Calendar.DAY_OF_MONTH))

        val gregorianCalendar =
            GregorianCalendar(
                tempCalendar.get(Calendar.YEAR),
                tempCalendar.get(Calendar.MONTH),
                tempCalendar.get((Calendar.DAY_OF_MONTH)),
                hourData.value!!,
                minuteData.value!!
            )
        gregorianCalendar.timeZone = TimeZone.getTimeZone(TaskDetailsElements.TIMEZONE.detailsElement)
        val calendar = (gregorianCalendar.timeInMillis) / miliToSecondDivide
        val timestamp = Timestamp(calendar, 0)
        taskCreatorDataManager.onUpdateTimestamp(timestamp)
        taskCreatorDataManager.onUpdateTimestampEstimated(false)
    }
}
