package eu.agribot.agribotapp.ui.fragment.pathmanagement

import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentPathManagementBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PathManagementFragment :
    BaseFragment<PathManagementViewModel, FragmentPathManagementBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_path_management
    override val viewModel: PathManagementViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentPathManagementBinding) {
        viewBinding.viewModel = viewModel
    }
}
