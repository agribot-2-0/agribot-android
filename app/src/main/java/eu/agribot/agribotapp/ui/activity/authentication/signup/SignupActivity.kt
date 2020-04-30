package eu.agribot.agribotapp.ui.activity.authentication.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.MainActivityNavigationEvent
import eu.agribot.agribotapp.databinding.ActivitySignupBinding
import eu.agribot.agribotapp.ui.activity.authentication.login.LoginActivity
import eu.agribot.agribotapp.ui.activity.authentication.welcome.WelcomeActivity
import eu.agribot.agribotapp.ui.activity.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupActivity : BaseActivity<SignupViewModel, ActivitySignupBinding>() {
    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, SignupActivity::class.java)
        }
    }

    override val layoutResourceId: Int = R.layout.activity_signup
    override val viewModel: SignupViewModel by viewModel()

    override fun bindViewVariables(viewBinding: ActivitySignupBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.showToastEvent.observe(this::showToast)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
        startActivity(WelcomeActivity.getStartingIntent(this))
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is LoginActivityNavigationEvent -> handleLoginActivityNavigationEvent()
        is MainActivityNavigationEvent -> handleMainActivityNavigationEvent()
        else -> Unit
    }

    fun handleMainActivityNavigationEvent() {
        this.finish()
        startActivity(MainActivity.getStartingIntent(this))
    }

    fun handleLoginActivityNavigationEvent() {
        this.finish()
        startActivity(LoginActivity.getStartingIntent(this))
    }

    fun showToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }
}
