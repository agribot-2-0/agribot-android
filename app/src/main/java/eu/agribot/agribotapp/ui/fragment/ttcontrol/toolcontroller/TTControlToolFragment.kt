package eu.agribot.agribotapp.ui.fragment.ttcontrol.toolcontroller

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.communication.bluetooth.basic.message.toolcontrolmessage.Tools
import eu.agribot.agribotapp.databinding.FragmentTtcontrolToolBinding
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.data.TplSwitchesState
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.manualcontrol.viewpager.toolcontrol.recycler.HitchListItemAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TTControlToolFragment : BaseFragment<TTControlToolViewModel, FragmentTtcontrolToolBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_ttcontrol_tool
    override val viewModel: TTControlToolViewModel by viewModel { parametersOf(context) }

    override fun bindViewVariables(viewBinding: FragmentTtcontrolToolBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observeViewModel()
        setCheckboxListeners()
        setSeekbarListener()
    }

    private val adapter = HitchListItemAdapter(this::onToolItemParamChanged)

    private fun setupRecycler() {
        viewBinding.toolHitchRecycler.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.toolsSwitchList.observe(this::addToolsSwitchItems)
        viewModel.switchesActive.observe(this::showSwitchesState)
    }

    private fun addToolsSwitchItems(items: List<HitchListItemViewModel>) {
        adapter.items = items
    }

    private fun onToolItemParamChanged(itemNumber: Int, sliderValue: Int, isActivated: Boolean) {
        viewModel.onHitchParametersChanged(itemNumber, sliderValue, isActivated)
    }

    private fun setSeekbarListener() {
        viewBinding.mainProgressBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                setSeekbarText(progress)
                viewModel.onTplValueChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // not used
            }
        })
    }

    private fun setSeekbarText(progress: Int) {
        viewBinding.seekbarText.text = "$progress %"
    }

    private fun setCheckboxListeners() {
        viewBinding.leftUpCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.mainProgressBar.progress = 50
            viewModel.onActivateTplParametrChanged(Tools.TplHeight.Front.toolId, isChecked)
        }
        viewBinding.leftDownCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.mainProgressBar.progress = 50
            viewModel.onActivateTplParametrChanged(Tools.TplAngle.Front.toolId, isChecked)
        }

        viewBinding.rightUpCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.mainProgressBar.progress = 50
            viewModel.onActivateTplParametrChanged(Tools.TplHeight.Rear.toolId, isChecked)
        }
        viewBinding.rightDownCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.mainProgressBar.progress = 50
            viewModel.onActivateTplParametrChanged(Tools.TplAngle.Rear.toolId, isChecked)
        }
    }

    private fun showSwitchesState(tplSwitchesState: TplSwitchesState) {
        viewBinding.rightDownCheckbox.isChecked = tplSwitchesState.rearBottomTplChecked
        viewBinding.leftUpCheckbox.isChecked = tplSwitchesState.frontUpTplChecked
        viewBinding.leftDownCheckbox.isChecked = tplSwitchesState.frontBottomTplChecked
        viewBinding.rightUpCheckbox.isChecked = tplSwitchesState.rearUpTplChecked
    }
}
