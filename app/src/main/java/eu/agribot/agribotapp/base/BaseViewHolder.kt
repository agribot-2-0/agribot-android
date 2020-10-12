package eu.agribot.agribotapp.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<
        viewModelType : BaseViewModel,
        viewDataBindingType : ViewDataBinding>(
            val viewBinding: viewDataBindingType
        ) : RecyclerView.ViewHolder(viewBinding.root) {
    abstract fun bind(viewModel: viewModelType)
}
