package eu.agribot.agribotapp.ui.activity.authentication.welcome

import android.content.Context
import android.content.Intent
import android.widget.Toast
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.LoginActivityNavigationEvent
import eu.agribot.agribotapp.common.navigation.events.SignupActivityNavigationEvent
import eu.agribot.agribotapp.databinding.ActivityWelcomeBinding
import eu.agribot.agribotapp.ui.activity.authentication.login.LoginActivity
import eu.agribot.agribotapp.ui.activity.authentication.signup.SignupActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeActivity : BaseActivity<WelcomeViewModel, ActivityWelcomeBinding>() {
    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, WelcomeActivity::class.java)
        }
    }

    override val layoutResourceId: Int = R.layout.activity_welcome
    override val viewModel: WelcomeViewModel by viewModel()

    override fun bindViewVariables(viewBinding: ActivityWelcomeBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is LoginActivityNavigationEvent -> handleLoginActivityNavigationEvent()
        is SignupActivityNavigationEvent -> handleSignupActivityNavigationEvent()
        else -> Unit
    }

    override fun onBackPressed() {
        Toast.makeText(this, getString(R.string.toast_cant_do_this), Toast.LENGTH_LONG).show()
    }

    private fun handleLoginActivityNavigationEvent() {
        this.finish()
        startActivity(LoginActivity.getStartingIntent(this))
    }

    private fun handleSignupActivityNavigationEvent() {
        this.finish()
        startActivity(SignupActivity.getStartingIntent(this))
    }
}
