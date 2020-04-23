package eu.agribot.agribotapp.ui.activity.main

import androidx.lifecycle.MutableLiveData
import eu.agribot.agribotapp.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    val loginStatus: MutableLiveData<String> = MutableLiveData()

    fun setLoginStatus(login: String) {
        loginStatus.value = login
    }
}
