package eu.agribot.agribotapp.ui.fragment.manualcontrol

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentManualControlBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ManualControlFragment : BaseFragment<ManualControlViewModel, FragmentManualControlBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_manual_control
    override val viewModel: ManualControlViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentManualControlBinding) {
        viewBinding.viewModel = viewModel
    }
}
