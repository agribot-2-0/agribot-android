package eu.agribot.agribotapp.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import timber.log.Timber

abstract class BaseFragment<viewModelType : BaseViewModel, viewDataBindingType : ViewDataBinding> :
    Fragment() {

    /**
     * Resource ID of layout
     * Will be inflated with view binding
     * When fragment's view is started
     */
    abstract val layoutResourceId: Int

    /**
     * Reference to ViewModel
     * Has to be injected into dereiving fragment
     */
    abstract val viewModel: viewModelType

    /**
     * Reference to view binding
     * Can be used to access view elements statically
     */
    lateinit var viewBinding: viewDataBindingType

    /**
     * Navigation controller instance
     * Retreived from fragment's view
     */
    var navigationController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflateViewWithBinding(inflater, container)
        return viewBinding.root
    }

    private fun inflateViewWithBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        viewBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        viewBinding.lifecycleOwner = this
        bindViewVariables(viewBinding)
    }

    /**
     * Can be overriden in dereiving class
     * To bind view variables to instance of ViewBinding
     */
    open fun bindViewVariables(viewBinding: viewDataBindingType) = Unit

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeNavigationEvents()
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findNavController(view)
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Attempts to retreive instance of NavController
     * Attached to fragment's view
     */
    private fun findNavController(view: View) {
        try {
            navigationController = Navigation.findNavController(view)
        } catch (exception: Exception) {
            Timber.w("Could not find navigation controller")
        }
    }

    fun <T> LiveData<out T>.observe(onEvent: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer(onEvent))
    }

    fun showToast(toastText: String) {
        Toast.makeText(activity, toastText, Toast.LENGTH_LONG).show()
    }

}
