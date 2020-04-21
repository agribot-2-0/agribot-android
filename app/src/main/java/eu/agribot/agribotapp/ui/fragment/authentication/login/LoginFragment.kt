package eu.agribot.agribotapp.ui.fragment.authentication.login

import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentLoginBinding
import eu.agribot.agribotapp.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentLoginBinding) {
        viewBinding.viewModel = viewModel
    }
}