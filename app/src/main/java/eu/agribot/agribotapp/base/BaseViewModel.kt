package eu.agribot.agribotapp.base

import androidx.lifecycle.ViewModel
import eu.agribot.agribotapp.common.livedata.NavigationLiveEvent

/**
 * Base class of ViewModels inside project
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * Observed in base activity/fragment classes
     * Value post causes `handleNavigationEvent()` call
     * @see BaseActivity
     */
    val navigationEvent: NavigationLiveEvent = NavigationLiveEvent()
}
