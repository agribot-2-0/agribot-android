package eu.agribot.agribotapp.ui.fragment.pathmanagement.pathname

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentPathManagementNameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PathNameFragment :
    BaseFragment<PathNameViewModel, FragmentPathManagementNameBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_path_management_name

    override val viewModel: PathNameViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentPathManagementNameBinding) {
        viewBinding.viewModel = viewModel
    }

}
