package eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ToolNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentToolManagementParamsBinding
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemAdapter
import eu.agribot.agribotapp.ui.fragment.toolcontrol.recycler.HitchListItemViewModel
import eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary.recycler.ReceivedRfidDataAdapter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.toolsummary.recycler.ReceivedRfidDataViewModel
import kotlinx.android.synthetic.main.fragment_display_tool_photo.*
import kotlinx.android.synthetic.main.fragment_tool_management_params.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class ToolParamsFragment :
    BaseFragment<ToolParamsViewModel, FragmentToolManagementParamsBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_tool_management_params
    override val viewModel: ToolParamsViewModel by viewModel { parametersOf(requireContext()) }
    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }
    private var rfidDataAdapter =
        ReceivedRfidDataAdapter(onItemCLicked = this::onRfidDataElementClicked)
    private val hitchRecyclerAdapter =
        HitchListItemAdapter(this::onHitchRecyclerParametersChanged)

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.createToolsList()
        setupRecyclers()


        val imgFile = File(getOutputDirectory().toString() + viewModel.getToolPhotoName())

        Log.e("xd", "image file: " + imgFile)

        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            val myImage = tool_photo
            myImage.setImageBitmap(myBitmap)
        }


    }

    @ExperimentalCoroutinesApi
    private fun observeViewModel() {
        viewModel.paramsList.observe(this::addParams)
        viewModel.noRfidTagDataEvent.observe(this::showNoRfidDialogBox)
       // viewModel.isScanOperation.observe(this::getButtonIdNumber)
        viewModel.rfidReceivedData.observe(this::appendRfidTagData)
    }

    private fun showNoRfidDialogBox(isNoFoundTagOperation: Boolean) {
        if (isNoFoundTagOperation) {
            dialogManager.showNoFoundRfidTagDialog(onRepeatScanConfirmed = viewModel::startRfidScan)
        }
    }

    private fun onRfidDataElementClicked(rfidElement: ReceivedRfidDataViewModel) {
        viewModel.onRfidTagSelected(rfidElement)
    }

    /*
    @ExperimentalCoroutinesApi
    private fun getButtonIdNumber(isScanOperation: Boolean) {
        if (isScanOperation)
            when (antennaGroup.checkedRadioButtonId) {
                frontAntenna.id -> {
                    viewModel.scanRfid(AntennaNumber.FRONT_ANTENNA.number)
                }
                backAntenna.id -> {
                    viewModel.scanRfid(AntennaNumber.BACK_ANTENNA.number)
                }
                bothAntenna.id -> {
                    viewModel.scanRfid(AntennaNumber.BOTH_ANTENNAS.number)
                }
                else -> showToast(getString(R.string.no_antenna_selected))
            }
    }


     */
    private fun setupRecyclers() {
        params_recycler.adapter = hitchRecyclerAdapter
        params_recycler.layoutManager = LinearLayoutManager(requireActivity())

        /*
        tagRfidRecycler.adapter = rfidDataAdapter
        tagRfidRecycler.layoutManager = LinearLayoutManager(requireActivity())

         */
    }

    private fun appendRfidTagData(tagData: List<ReceivedRfidDataViewModel>) {
        rfidDataAdapter.items = tagData
    }

    private fun onHitchRecyclerParametersChanged(
        sliderNumber: Int,
        sliderValue: Int,
        isActivated: Boolean
    ) {
        viewModel.onHitchParametersChanged(sliderNumber, sliderValue)
    }

    private fun addParams(tools: List<HitchListItemViewModel>) {
        hitchRecyclerAdapter.items = tools
    }

    override fun bindViewVariables(viewBinding: FragmentToolManagementParamsBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ToolNavigationEvent -> navigateToToolManagement()
        else -> Unit
    }

    private fun navigateToToolManagement() {
        navigationController?.navigate(R.id.action_toolManagementParamsFragment_to_toolManagementFragment)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }
}
