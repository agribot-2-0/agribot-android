package eu.agribot.agribotapp.ui.fragment.authentication.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentLoginBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showToastEvent.observe(this::showToast)
    }

    fun showToast(toastText: String) {
        Toast.makeText(activity, toastText, Toast.LENGTH_LONG).show()
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolControlNavigationEvent -> backToRegister()
        else -> Unit
    }

    fun backToRegister() {
        navigationController?.navigate(R.id.action_LoginFragment_to_SignupFragment)
    }
}
