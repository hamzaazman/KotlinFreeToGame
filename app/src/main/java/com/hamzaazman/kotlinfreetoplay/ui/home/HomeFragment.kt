package com.hamzaazman.kotlinfreetoplay.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.hamzaazman.kotlinfreetoplay.HorizontalItemDecoration
import com.hamzaazman.kotlinfreetoplay.VerticalItemDecoration
import com.hamzaazman.kotlinfreetoplay.common.viewBinding
import com.hamzaazman.kotlinfreetoplay.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(com.hamzaazman.kotlinfreetoplay.R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val vm by viewModels<HomeViewModel>()
    private val homeAdapter by lazy { HomeAdapter() }

    private var checkedCategory = "all"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRv()
        uiState()
        selectCategory()
        vm.getAllGame()
    }

    private fun selectCategory() = with(binding) {
        categoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            checkedIds.forEach {
                checkedCategory = group.findViewById<Chip>(it).text.toString().lowercase()
                lifecycleScope.launch {
                    vm.getGameByCategory(checkedCategory)
                }
            }
        }
    }

    private fun setupRv() = with(binding) {
        gameRecycler.apply {
            adapter = homeAdapter
            setHasFixedSize(false)
            addItemDecoration(VerticalItemDecoration(26))
            addItemDecoration(HorizontalItemDecoration(42))
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun uiState() = with(binding) {
        lifecycleScope.launch {
            vm.gameList.collectLatest { state ->
                when (state) {
                    is HomeUiState.Success -> {
                        gameError.visibility = View.GONE
                        gameRecycler.visibility = View.VISIBLE
                        homeAdapter.submitList(state.data)
                    }

                    is HomeUiState.Error -> {
                        gameRecycler.visibility = View.GONE
                        gameError.apply {
                            visibility = View.VISIBLE
                            text = state.message
                        }
                    }

                    is HomeUiState.Loading -> {
                        gameError.visibility = View.GONE
                        gameRecycler.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}