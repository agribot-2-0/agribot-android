package eu.agribot.agribotapp.ui.fragment.authentication.signup

import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentSignupBinding
import eu.agribot.agribotapp.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignupFragment : BaseFragment<SignupViewModel, FragmentSignupBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_signup
    override val viewModel: SignupViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentSignupBinding) {
        viewBinding.viewModel = viewModel
    }
}