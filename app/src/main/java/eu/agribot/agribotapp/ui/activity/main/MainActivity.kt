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
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.mainDrawerLayout
import kotlinx.android.synthetic.main.activity_main.mainDrawerNV
import kotlinx.android.synthetic.main.activity_main.mainToolbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override val layoutResourceId: Int = R.layout.activity_main
    override val navigationHostLayoutId: Int = R.id.mainFCV
    override val viewModel: MainViewModel by viewModel()
    private val dialogManager: DialogManager by inject { parametersOf(this) }

    /*
    List of destinations that should not return to previous
    Screen when back is pressed
    */
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

    /*
    Closes drawer and handles selected option
    When navigation drawer item is selected
     */
    private fun setNavigationDrawerActionClickListener(navController: NavController) {
        mainDrawerNV.setNavigationItemSelectedListener { item ->
            mainDrawerLayout.closeDrawer(GravityCompat.START)
            item.onNavDestinationSelected(navController)
        }
    }

    /*
    If navigation component is present, call it to navigate up
    Else use system's native solution
     */
    override fun onNavigateUp(): Boolean {
        return navigationController?.let {
            NavigationUI.navigateUp(it, appBarConfiguration)
        } ?: run {
            super.onNavigateUp()
        }
    }

    /*
    If drawer is open, close it
    In other cases handle fragment state navigation
     */
    override fun onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            handleBackPressed()
        }
    }

    /*
    If NavController has a registered destination
    Check if it's top-level destination
    If so, show exit prompt and ask user for confirmation
    In other cases, call system back press
     */
    private fun handleBackPressed() {
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

    private fun showExitDialog() {
        dialogManager.showExitDialog {
            this.finishAffinity()
        }
    }
}
