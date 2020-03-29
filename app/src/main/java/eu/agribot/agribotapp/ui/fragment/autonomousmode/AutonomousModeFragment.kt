package eu.agribot.agribotapp.ui.fragment.autonomousmode

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentAutonomousModeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AutonomousModeFragment :
    BaseFragment<AutonomousModeViewModel, FragmentAutonomousModeBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_autonomous_mode
    override val viewModel: AutonomousModeViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentAutonomousModeBinding) {
        viewBinding.viewModel = viewModel
    }
}
