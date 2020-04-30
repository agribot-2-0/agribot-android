package eu.agribot.agribotapp.ui.activity.authentication.passwordreminder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import eu.agribot.agribotapp.R
import android.widget.Toast
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.databinding.ActivityPasswordReminderBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordReminderActivity : BaseActivity<PasswordReminderViewModel, ActivityPasswordReminderBinding>() {
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

    fun showToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }
}
