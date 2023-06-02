package com.hamzaazman.kotlinfreetoplay.ui.home

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hamzaazman.kotlinfreetoplay.HorizontalItemDecoration
import com.hamzaazman.kotlinfreetoplay.R
import com.hamzaazman.kotlinfreetoplay.VerticalItemDecoration
import com.hamzaazman.kotlinfreetoplay.capitalizeFirstLetter
import com.hamzaazman.kotlinfreetoplay.common.viewBinding
import com.hamzaazman.kotlinfreetoplay.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val vm by viewModels<HomeViewModel>()
    private val homeAdapter by lazy {
        HomeAdapter { gameUi ->
            val action =
                HomeFragmentDirections.actionNavigationHomeToDetailFragment(gameId = gameUi.id)
            findNavController().navigate(action)
        }
    }

    private var checkedCategory = ""
    private var checkedCategoryId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRv()
        selectCategory()
        uiState()
    }

    private fun selectCategory() = with(binding) {
        if (checkedCategory.isEmpty()) {
            vm.getAllGame()
        }
        vm.getCategoryAndId.observe(viewLifecycleOwner) { categoryType ->
            clearChip.visibility =
                if (categoryType.checkedCategory == "all") View.GONE else View.VISIBLE

            checkedCategory = categoryType.checkedCategory.apply {
                if (!this.contains("clear filter")) {
                    toolbarTextView.text = this.capitalizeFirstLetter()
                }
            }
            checkedCategoryId = categoryType.checkedCategoryId
            updateChip(checkedCategoryId, categoryChipGroup)

        }
        categoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            checkedIds.forEach {
                checkedCategory = group.findViewById<Chip>(it).text.toString().lowercase()
                lifecycleScope.launch {
                    vm.saveCategoryAndId(category = checkedCategory, categoryId = it)
                    if (!checkedCategory.contains("clear filter")) {
                        vm.getGameByCategory(checkedCategory)
                    }
                }
                clearChip.visibility = View.VISIBLE

                val transition = ChangeBounds()
                transition.duration = 200
                TransitionManager.beginDelayedTransition(
                    categoryChipGroup.parent as ViewGroup,
                    transition
                )
            }
        }

        clearChip.setOnClickListener {
            clearChip.visibility = View.GONE
            lifecycleScope.launch {
                vm.clearCategoryFilter()
                vm.getAllGame()
            }
        }
    }


    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                binding.gameError.text = e.message
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
                        shimmerViewContainer.apply {
                            stopShimmer()
                            visibility = View.GONE
                        }
                        homeAdapter.submitList(state.data)
                    }

                    is HomeUiState.Error -> {
                        gameRecycler.visibility = View.GONE
                        gameError.apply {
                            visibility = View.VISIBLE
                            text = state.message
                        }
                        shimmerViewContainer.apply {
                            stopShimmer()
                            visibility = View.GONE
                        }
                    }

                    is HomeUiState.Loading -> {
                        gameError.visibility = View.GONE
                        gameRecycler.visibility = View.GONE
                        shimmerViewContainer.apply {
                            startShimmer()
                            visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

}