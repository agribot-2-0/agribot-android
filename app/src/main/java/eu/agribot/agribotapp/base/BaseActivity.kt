package eu.agribot.agribotapp.base

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.ninenox.kotlinlocalemanager.ApplicationLocale
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import timber.log.Timber

/**
 * Base class of activities inside project
 */
abstract class BaseActivity<viewModelType : BaseViewModel, viewDataBindingType : ViewDataBinding> :
 FragmentActivity() {

    /**
     * Resource ID of layout
     * Will be inflated with view binding
     * When activity is started
     */
    abstract val layoutResourceId: Int

    /**
     * Reference to ViewModel
     * Has to be injected into dereiving activity
     */
    abstract val viewModel: viewModelType

    /**
     * Reference to view binding
     * Can be used to access view elements statically
     */
    lateinit var viewBinding: viewDataBindingType

    /**
     * ID of navigation controller layout host
     * Can be left null if no NavHost is attached to view
     */
    open val navigationHostLayoutId: Int? = null

    /**
     * Navigation controller instance
     * Retreived from NavHost layout ID
     */
    var navigationController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateViewWithBinding()
        observeNavigationEvents()
    }

    private fun inflateViewWithBinding() {
        viewBinding = DataBindingUtil.setContentView(this, layoutResourceId)
        bindViewVariables(viewBinding)
    }

    /**
     * Can be overriden in dereiving class
     * To bind view variables to instance of ViewBinding
     */
    open fun bindViewVariables(viewBinding: viewDataBindingType) = Unit

    /**
     * Observes all events emitted on ViewModel
     */
    private fun observeNavigationEvents() {
        viewModel.navigationEvent.observe(this::handleNavigationEvent)
    }

    /**
     * Can be overriden in dereiving class
     * Fires when navigation event is published in ViewModel
     */
    open fun handleNavigationEvent(navigationEvent: NavigationEvent) = Unit

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        findNavigationController()
    }

    /**
     * Attempts to retreive instance of NavController
     * From specified NavHost layout resource
     */
    private fun findNavigationController() {
        navigationHostLayoutId?.let {
            try {
                navigationController = findNavController(it)
            } catch (exception: Exception) {
                Timber.w("Could not find navigation controller")
            }
        }
    }

    fun <T> LiveData<out T>.observe(onEvent: (T) -> Unit) {
        observe(this@BaseActivity, Observer(onEvent))
    }

}
