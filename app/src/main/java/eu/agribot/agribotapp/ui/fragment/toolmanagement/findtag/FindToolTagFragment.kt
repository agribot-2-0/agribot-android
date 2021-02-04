package eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.common.dialog.DialogManager
import eu.agribot.agribotapp.common.navigation.NavigationEvent
import eu.agribot.agribotapp.common.navigation.events.ShotToolPhotoNavigationEvent
import eu.agribot.agribotapp.databinding.FragmentFindToolTagBinding
import eu.agribot.agribotapp.ui.fragment.toolmanagement.findtag.data.AntennaNumber
import eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparameters.recycler.ReceivedRfidDataAdapter
import eu.agribot.agribotapp.ui.fragment.toolmanagement.toolparameters.recycler.ReceivedRfidDataViewModel
import kotlinx.android.synthetic.main.fragment_find_tool_tag.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FindToolTagFragment :
    BaseFragment<FindToolTagViewModel, FragmentFindToolTagBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_find_tool_tag
    override val viewModel: FindToolTagViewModel by viewModel { parametersOf(requireContext()) }
    private val dialogManager: DialogManager by inject { parametersOf(requireContext()) }
    private var rfidDataAdapter =
        ReceivedRfidDataAdapter(onItemCLicked = this::onRfidDataElementClicked)

    override fun bindViewVariables(viewBinding: FragmentFindToolTagBinding) {
        viewBinding.viewModel = viewModel
    }


    override fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        is ShotToolPhotoNavigationEvent -> navigateToShotPhoto()
        else -> Unit
    }

    private fun navigateToShotPhoto() {
        navigationController?.navigate(R.id.action_findToolFragment_to_shotToolPhoto)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
       observeViewModel()

    }

    @ExperimentalCoroutinesApi
    private fun observeViewModel(){
        viewModel.isScanOperation.observe(this::getButtonIdNumber)
        viewModel.rfidReceivedData.observe(this::appendRfidTagData)
        viewModel.noRfidTagDataEvent.observe(this::showNoRfidDialogBox)

    }

    private fun appendRfidTagData(tagData: List<ReceivedRfidDataViewModel>) {
        rfidDataAdapter.items = tagData
    }


    private fun setupRecycler(){
        found_tool_recycler.adapter = rfidDataAdapter
        found_tool_recycler.layoutManager = LinearLayoutManager(requireActivity())
    }

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
                else -> showToast(getString(R.string.no_antenna_selected))
            }
    }

    private fun onRfidDataElementClicked(rfidElement: ReceivedRfidDataViewModel) {
        viewModel.onRfidTagSelected(rfidElement)
    }

    private fun showNoRfidDialogBox(isNoFoundTagOperation: Boolean) {
        if (isNoFoundTagOperation) {
            dialogManager.showNoFoundRfidTagDialog(onRepeatScanConfirmed = viewModel::startRfidScan)
        }
    }



}
