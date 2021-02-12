package eu.agribot.agribotapp.ui.fragment.controlPanel.viewpager.taskdetails

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.databinding.PagerTaskDetailsBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PagerTaskDetailsFragment :
    BaseFragment<PagerTaskDetailsViewModel, PagerTaskDetailsBinding>() {
    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }
    override val layoutResourceId: Int = R.layout.pager_task_details
    override val viewModel: PagerTaskDetailsViewModel by viewModel { parametersOf(requireContext()) }

    override fun bindViewVariables(viewBinding: PagerTaskDetailsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getActualTask()
        viewModel.cancelTaskButtonEvent.observe(this::onCancelTask)
    }

    private fun onCancelTask(cancelTask: Boolean) {
        if (cancelTask) {
            dialogManager.cancelTaskDialog(viewModel::sendCancelTask)
        }
    }
}
