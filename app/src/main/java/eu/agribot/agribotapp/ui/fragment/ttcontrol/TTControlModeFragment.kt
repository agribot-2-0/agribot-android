package eu.agribot.agribotapp.ui.fragment.ttcontrol

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentTtcontrolBinding
import kotlinx.android.synthetic.main.fragment_ttcontrol.front_kart
import kotlinx.android.synthetic.main.fragment_ttcontrol.horizontal_joystick
import kotlinx.android.synthetic.main.fragment_ttcontrol.rear_kart
import kotlinx.android.synthetic.main.fragment_ttcontrol.vertical_joystick
import org.koin.androidx.viewmodel.ext.android.viewModel

class TTControlModeFragment : BaseFragment<TTControlViewModel, FragmentTtcontrolBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_ttcontrol
    override val viewModel: TTControlViewModel by viewModel()

    override fun bindViewVariables(viewBinding: FragmentTtcontrolBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setDefaultCartState()
        viewModel.frontCart.observe(this::onFrontKartChanged)
        viewModel.rearCart.observe(this::onRearKartChanged)
        horizontal_joystick.setOnMoveListener { angle, _ ->
            viewModel.onHorizontalJoystickParametersChanged(angle)
        }
        vertical_joystick.setOnMoveListener { angle, _ ->
            viewModel.onVerticalJoystickParametersChanged(angle)
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
