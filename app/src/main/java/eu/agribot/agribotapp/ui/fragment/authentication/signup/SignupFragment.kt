package eu.agribot.agribotapp.ui.fragment.authentication.signup

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolControlNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentSignupBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupFragment : BaseFragment<SignupViewModel, FragmentSignupBinding>() {
    private lateinit var auth: FirebaseAuth

    override val layoutResourceId: Int = R.layout.fragment_signup
    override val viewModel: SignupViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentSignupBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolControlNavigationEvent -> goToLoginFragment()
        else -> Unit
    }

    fun goToLoginFragment() {
        navigationController?.navigate(R.id.action_SignupFragment_to_LoginFragment)
    }
}
