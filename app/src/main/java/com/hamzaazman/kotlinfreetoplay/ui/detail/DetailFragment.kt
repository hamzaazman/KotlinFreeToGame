package com.hamzaazman.kotlinfreetoplay.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hamzaazman.kotlinfreetoplay.R
import com.hamzaazman.kotlinfreetoplay.common.viewBinding
import com.hamzaazman.kotlinfreetoplay.databinding.FragmentDetailBinding
import com.hamzaazman.kotlinfreetoplay.extractYearFromDateString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val vm by viewModels<DetailViewModel>()
    private val args: DetailFragmentArgs by navArgs()
    private val reviewAdapter by lazy { ReviewAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.getGameDetailById(args.gameId)
            }
        }

        detailUiState()

    }

    private fun detailUiState() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.detailData.collect { response ->
                when (response) {
                    is DetailUiState.Loading -> {}
                    is DetailUiState.Error -> {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is DetailUiState.Success -> {
                        response.data?.let { data ->
                            Glide.with(requireContext())
                                .load(data.thumbnail)
                                .placeholder(R.drawable.game_placeholder)
                                .into(detailImageView)

                            detailTitle.text = data.title
                            detailGenre.text = data.genre
                            detailPlatform.text = data.platform
                            detailReleaseDate.text = data.releaseDate.extractYearFromDateString()
                            detailDesc.text = data.description

                            detailMinOS.text = data.minimumSystemRequirements.os
                            detailMinProcessor.text = data.minimumSystemRequirements.processor
                            detailMinMemory.text = data.minimumSystemRequirements.memory
                            detailMinStorage.text = data.minimumSystemRequirements.storage

                            screenShootRecycler.adapter = reviewAdapter
                            reviewAdapter.submitList(data.screenshots)
                        }
                    }

                }
            }
        }
    }
}

