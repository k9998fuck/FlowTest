package com.k9998.flowtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.k9998.flowtest.databinding.ActivityMainBinding
import com.k9998.flowtest.databinding.ItemUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this

        val adapter = object : ListAdapter<UsersItemUiState, BaseViewHolder<ItemUserBinding>>(object :
            DiffUtil.ItemCallback<UsersItemUiState>() {
            override fun areItemsTheSame(oldItem: UsersItemUiState, newItem: UsersItemUiState): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UsersItemUiState, newItem: UsersItemUiState): Boolean {
                return oldItem.user.name == newItem.user.name
            }

        }) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): BaseViewHolder<ItemUserBinding> {
                return BaseViewHolder(
                    ItemUserBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            override fun onBindViewHolder(holder: BaseViewHolder<ItemUserBinding>, position: Int) {
                holder.viewBinding.usersItemUiState = getItem(position)
            }

        }
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            mainViewModel.result.collect {
                adapter.submitList(it.users)
            }
        }
    }


}