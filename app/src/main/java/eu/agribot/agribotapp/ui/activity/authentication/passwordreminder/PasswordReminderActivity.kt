package eu.agribot.agribotapp.ui.activity.authentication.passwordreminder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.databinding.ActivityPasswordReminderBinding
import eu.agribot.agribotapp.ui.activity.authentication.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordReminderActivity :
    BaseActivity<PasswordReminderViewModel, ActivityPasswordReminderBinding>() {
    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, PasswordReminderActivity::class.java)
        }
    }

    override val layoutResourceId: Int = R.layout.activity_password_reminder
    override val viewModel: PasswordReminderViewModel by viewModel()

    override fun bindViewVariables(viewBinding: ActivityPasswordReminderBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.showToastEvent.observe(this::showToast)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is LoginActivityNavigationEvent -> handleLoginActivityNavigationEvent()
        else -> Unit
    }

    fun handleLoginActivityNavigationEvent() {
        this.finish()
        startActivity(LoginActivity.getStartingIntent(this))
    }

    fun showToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }
}
