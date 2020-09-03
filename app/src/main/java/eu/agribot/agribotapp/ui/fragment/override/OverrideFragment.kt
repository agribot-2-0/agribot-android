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


}
