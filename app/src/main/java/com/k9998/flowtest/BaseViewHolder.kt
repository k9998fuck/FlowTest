package com.k9998.flowtest

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder<T : ViewBinding>(val viewBinding: T) : RecyclerView.ViewHolder(viewBinding.root)