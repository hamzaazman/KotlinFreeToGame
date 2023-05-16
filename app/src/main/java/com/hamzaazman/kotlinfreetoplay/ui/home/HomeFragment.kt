package com.hamzaazman.kotlinfreetoplay.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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

    private var checkedCategory = ""
    private var checkedCategoryId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRv()
        selectCategory()
        uiState()
    }

    private fun selectCategory() = with(binding) {
        if (checkedCategory.isNullOrEmpty()) {
            vm.getAllGame()
        }
        vm.getCategoryAndId.asLiveData().observe(viewLifecycleOwner) { categoryType ->
            checkedCategory = categoryType.checkedCategory
            checkedCategoryId = categoryType.checkedCategoryId
            updateChip(checkedCategoryId, categoryChipGroup)

        }

        categoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            checkedIds.forEach {
                checkedCategory = group.findViewById<Chip>(it).text.toString().lowercase()
                lifecycleScope.launch {
                    vm.saveCategoryAndId(category = checkedCategory, categoryId = it)
                    vm.getGameByCategory(checkedCategory)
                }
            }
        }
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
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