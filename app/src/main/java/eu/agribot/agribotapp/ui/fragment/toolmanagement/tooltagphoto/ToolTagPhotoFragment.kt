package eu.agribot.agribotapp.ui.fragment.toolmanagement.tooltagphoto

import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import eu.agribot.agribotapp.R
import eu.agribot.agribotapp.base.BaseFragment
import eu.agribot.agribotapp.databinding.FragmentCreatePhotoBinding
import kotlinx.android.synthetic.main.fragment_create_photo.view_finder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias LumaListener = (luma: Double) -> Unit

class ToolTagPhotoFragment : BaseFragment<ToolTagPhotoViewModel, FragmentCreatePhotoBinding>() {

    override val layoutResourceId: Int = R.layout.fragment_create_photo
    override val viewModel: ToolTagPhotoViewModel by viewModel { parametersOf(requireContext()) }
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun bindViewVariables(viewBinding: FragmentCreatePhotoBinding) {
        viewBinding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkPermissions(this::startCamera)
        viewModel.takePhotoEvent.observe(this::takePhoto)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(view_finder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                showToast(getString(R.string.use_camera_failed))
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto(isTakePhotoAction: Boolean) {
        if (isTakePhotoAction) {
            val imageCapture = imageCapture ?: return

            val outputOptions = ImageCapture.OutputFileOptions.Builder(
                File(
                    viewModel.getPhotoFile(),
                    viewModel.getToolPhotoName()
                )
            ).build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        showToast(getString(R.string.take_photo_failed))
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        navigateToDisplayPhotoFragment()
                    }
                })
        }
    }

    private fun navigateToDisplayPhotoFragment() {
        navigationController?.navigate(R.id.action_shotToolPhoto_to_displayToolPhoto)
    }
}
