package eu.agribot.agribotapp.ui.activity.authentication.passwordreminder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.databinding.ActivityPasswordReminderBinding
import eu.agribot.agribotapp.ui.activity.authentication.login.LoginActivity
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PasswordReminderActivity :
    BaseActivity<PasswordReminderViewModel, ActivityPasswordReminderBinding>() {
    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, PasswordReminderActivity::class.java)
        }
    }

    override val layoutResourceId: Int = R.layout.activity_password_reminder
    override val viewModel: PasswordReminderViewModel by viewModel { parametersOf(this) }

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

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
        startActivity(WelcomeActivity.getStartingIntent(this))
    }

    fun handleLoginActivityNavigationEvent() {
        this.finish()
        startActivity(LoginActivity.getStartingIntent(this))
    }
}
