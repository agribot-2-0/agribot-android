package eu.agribot.agribotapp.ui.fragment.toolcontrol

import android.os.Bundle
import android.view.View
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentToolControlBinding
import kotlinx.android.synthetic.main.fragment_tool_control.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToolControlFragment : BaseFragment<ToolControlViewModel, FragmentToolControlBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_control
    override val viewModel: ToolControlViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentToolControlBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        left_up_bar.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onLeftUpTuzChange
            )
        )

        left_down_bar.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onLeftDownTuzChange
            )
        )

        right_up_bar.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onRightUpTuzChange
            )
        )

        right_down_bar.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onRightDownTuzChange
            )
        )

        left_pto_slider.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onLeftPTOChanged
            )
        )
        left_blue_slider.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onLeftBlueChanged
            )
        )
        left_yellow_slider.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onLeftYellowChanged
            )
        )
        left_green_slider.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onLeftGreenChanged
            )
        )

        right_pto_slider.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onRightPTOChanged
            )
        )
        right_blue_slider.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onRightBlueChanged
            )
        )
        right_yellow_slider.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onRightYellowChanged
            )
        )
        right_green_slider.setOnSeekBarChangeListener(
            ToolsBarChangeListener(
                this::showProgressToast,
                viewModel::onRightGreenChanged
            )
        )

        left_pto_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.leftPTOSwitch = isChecked
        }

        left_blue_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.leftBlueSwitch = isChecked
        }

        left_yellow_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.leftYellowSwitch = isChecked
        }

        left_green_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.leftGreenSwitch = isChecked
        }

        right_pto_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.rightPTOSwitch = isChecked
        }

        right_blue_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.rightBlueSwitch = isChecked
        }

        right_yellow_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.rightYellowSwitch = isChecked
        }

        right_green_switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.rightGreenSwitch = isChecked
        }
    }

    private fun showProgressToast(progress: Int) {
        showToast("$progress %")
    }
}
