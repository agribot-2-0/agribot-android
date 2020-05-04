package eu.agribot.agribotapp.ui.activity.authentication.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.PasswordReminderActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.SignupActivityNavigationEvent
import eu.agribot.agribotapp.databinding.ActivityLoginBinding
import eu.agribot.agribotapp.ui.activity.authentication.passwordreminder.PasswordReminderActivity
import eu.agribot.agribotapp.ui.activity.authentication.signup.SignupActivity
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeActivity
import eu.agribot.agribotapp.ui.activity.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override val layoutResourceId: Int = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModel { parametersOf(this) }
    override fun bindViewVariables(viewBinding: ActivityLoginBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.showToastEvent.observe(this::showToast)
    }

    fun showToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is SignupActivityNavigationEvent -> handleSignupActivityNavigationEvent()
        is PasswordReminderActivityNavigationEvent -> handlePasswordReminderActivityNavigationEvent()
        is MainActivityNavigationEvent -> handleMainActivityNavigationEvent()
        else -> Unit
    }

    fun handleMainActivityNavigationEvent() {
        this.finish()
        startActivity(MainActivity.getStartingIntent(this))
    }

    fun handleSignupActivityNavigationEvent() {
        this.finish()
        startActivity(SignupActivity.getStartingIntent(this))
    }

    fun handlePasswordReminderActivityNavigationEvent() {
        this.finish()
        startActivity(PasswordReminderActivity.getStartingIntent(this))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
        startActivity(WelcomeActivity.getStartingIntent(this))
    }
}
