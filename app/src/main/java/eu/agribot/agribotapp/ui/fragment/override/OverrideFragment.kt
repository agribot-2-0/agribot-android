package eu.agribot.agribotapp.ui.fragment.override

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentOverrideModeBinding
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.fragment_override_mode.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OverrideFragment : BaseFragment<OverrideViewModel, FragmentOverrideModeBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_override_mode
    override val viewModel: OverrideViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentOverrideModeBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onFrontClickEvent.observe(this::changeFrontTint)
        viewModel.onRearClickEvent.observe(this::changeRearTint)

        val leftJoystick = view.findViewById(R.id.left_right_hoy) as JoystickView
        leftJoystick.setOnMoveListener { angle, _ ->
            viewModel.onJoystickLeftParametrsChanged(angle)
        }

        val joystick = view.findViewById(R.id.joyy) as JoystickView
        joystick.setOnMoveListener { angle, _ ->
            viewModel.onJoystickParametrsChanged(angle)
        }
    }

    private fun changeFrontTint(isClicked: Boolean) {
        if (isClicked) {
            front.setColorFilter(ContextCompat.getColor(requireContext(), R.color.base_green))
            viewModel.setFront(true)
        } else {
            front.colorFilter = null
            viewModel.setFront(false)
        }
    }

    private fun changeRearTint(isClicked: Boolean) {
        if (isClicked) {
            viewModel.setRear(true)
            rear.setColorFilter(ContextCompat.getColor(requireContext(), R.color.base_green))
        } else {
            rear.colorFilter = null
            viewModel.setRear(false)
        }
    }
}
