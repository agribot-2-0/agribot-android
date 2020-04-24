package eu.agribot.agribotapp.ui.fragment.authentication.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentSignupBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupFragment : BaseFragment<SignupViewModel, FragmentSignupBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_signup
    override val viewModel: SignupViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentSignupBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showToastEvent.observe(this::showToast)
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolControlNavigationEvent -> goToLoginFragment()
        else -> Unit
    }

    fun goToLoginFragment() {
        navigationController?.navigate(R.id.action_SignupFragment_to_LoginFragment)
    }

    fun showToast(toastText: String) {
        Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show()
    }
}
