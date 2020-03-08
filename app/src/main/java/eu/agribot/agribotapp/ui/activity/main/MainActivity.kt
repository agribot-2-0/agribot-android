package eu.agribot.agribotapp.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.ui.NavigationUI.setupWithNavController
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.mainDrawerLayout
import kotlinx.android.synthetic.main.activity_main.mainToolbar
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override val layoutResourceId: Int = R.layout.activity_main
    override val navigationHostLayoutId: Int = R.id.mainFCV
    override val viewModel: MainViewModel by inject()

    override fun bindViewVariables(viewBinding: ActivityMainBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupNavigationViews()
    }

    private fun setupNavigationViews() {
        navigationController?.let { navigationController ->
            setupWithNavController(mainToolbar, navigationController, mainDrawerLayout)
        }
    }
}
