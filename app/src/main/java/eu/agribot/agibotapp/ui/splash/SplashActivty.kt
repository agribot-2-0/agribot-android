package eu.agribot.agibotapp.ui.splash

import eu.agribot.agibotapp.R
import eu.agribot.agibotapp.base.BaseActivity
import eu.agribot.agibotapp.databinding.ActivitySplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivty : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    override val layoutResourceId: Int = R.layout.activity_splash
    override val viewModel: SplashViewModel by viewModel()

    override fun bindViewVariables(viewBinding: ActivitySplashBinding) {
        super.bindViewVariables(viewBinding)
        viewBinding.viewModel = viewModel
    }
}
