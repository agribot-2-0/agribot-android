package eu.agribot.agribotapp.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.onNavDestinationSelected
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseActivity
import eu.agribot.agribotapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.mainDrawerLayout
import kotlinx.android.synthetic.main.activity_main.mainDrawerNV
import kotlinx.android.synthetic.main.activity_main.mainToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override val layoutResourceId: Int = R.layout.activity_main
    override val navigationHostLayoutId: Int = R.id.mainFCV
    override val viewModel: MainViewModel by viewModel()

    private val navigationTopLevelDestinations = setOf(
        R.id.manualControlFragment,
        R.id.autonomousModeFragment,
        R.id.toolManagementFragment,
        R.id.pathManagementFragment,
        R.id.weatherFragment,
        R.id.settingsFragment
    )

    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            topLevelDestinationIds = navigationTopLevelDestinations,
            drawerLayout = mainDrawerLayout
        )
    }

    override fun bindViewVariables(viewBinding: ActivityMainBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() = with(navigationController!!) {
        setupNavigationDrawerNavController(this)
        setupToolbarNavController(this)
        setInitialCheckedItem(this)
        setToolbarActionClickListener(this)
        setNavigationDrawerActionClickListener(this)
    }

    private fun setupNavigationDrawerNavController(navController: NavController) {
        setupWithNavController(
            mainDrawerNV,
            navController
        )
    }

    private fun setupToolbarNavController(navController: NavController) {
        setupWithNavController(
            mainToolbar,
            navController,
            appBarConfiguration
        )
    }

    private fun setInitialCheckedItem(navController: NavController) {
        mainDrawerNV.setCheckedItem(
            navController.graph.startDestination
        )
    }

    private fun setToolbarActionClickListener(navController: NavController) {
        mainToolbar.setOnMenuItemClickListener { item ->
            item.onNavDestinationSelected(navController)
        }
    }

    private fun setNavigationDrawerActionClickListener(navController: NavController) {
        mainDrawerNV.setNavigationItemSelectedListener { item ->
            mainDrawerLayout.closeDrawer(GravityCompat.START)
            item.onNavDestinationSelected(navController)
        }
    }

    override fun onNavigateUp(): Boolean {
        return navigationController?.let {
            NavigationUI.navigateUp(it, appBarConfiguration)
        } ?: run {
            super.onNavigateUp()
        }
    }

    override fun onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            navigationController?.currentDestination?.id?.let {
                if (navigationTopLevelDestinations.contains(it)) {
                    showExitDialog()
                } else {
                    super.onBackPressed()
                }
            } ?: run {
                super.onBackPressed()
            }
        }
    }

    private fun showExitDialog() {
        finish()
    }
}
