package eu.agribot.agribotapp.ui.fragment.ttcontrol

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentTtcontrolBinding
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.fragment_ttcontrol.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TTControlModeFragment : BaseFragment<TTControlViewModel, FragmentTtcontrolBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_ttcontrol
    override val viewModel: TTControlViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentTtcontrolBinding) {
        viewBinding.viewModel = viewModel
        viewModel.frontKart.observe(this::onFrontKartChanged)
        viewModel.rearKart.observe(this::onRearKartChanged)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val horizontalJoystick = horizontal_joystick as JoystickView
        horizontalJoystick.setOnMoveListener { angle, _ ->
            viewModel.onHorizontalJoystickParametrsChanged(angle)
        }
        val verticalJoystick = vertical_joystick as JoystickView
        verticalJoystick.setOnMoveListener { angle, _ ->
            viewModel.onVerticalJoystickParametrsChanged(angle)
        }
    }

    private fun onFrontKartChanged(isActive: Boolean) {
        if (isActive) {
            front_kart.setColorFilter(ContextCompat.getColor(requireContext(), R.color.base_green))
        } else {
            front_kart.colorFilter = null
        }
    }

    private fun onRearKartChanged(isActive: Boolean) {
        if (isActive) {
            rear_kart.setColorFilter(ContextCompat.getColor(requireContext(), R.color.base_green))
        } else {
            rear_kart.colorFilter = null
        }
    }
}
