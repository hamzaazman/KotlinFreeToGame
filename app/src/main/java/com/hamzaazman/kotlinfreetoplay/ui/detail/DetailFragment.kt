package com.hamzaazman.kotlinfreetoplay.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.hamzaazman.kotlinfreetoplay.R
import com.hamzaazman.kotlinfreetoplay.common.viewBinding
import com.hamzaazman.kotlinfreetoplay.databinding.FragmentDetailBinding
import com.hamzaazman.kotlinfreetoplay.extractYearFromDateString
import com.hamzaazman.kotlinfreetoplay.makeCollapsible
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

        binding.detailToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun detailUiState() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.detailData.collect { response ->
                when (response) {
                    is DetailUiState.Loading -> {
                        shimmerDetailContainer.startShimmer()
                        shimmerDetailContainer.visibility = View.VISIBLE
                    }

                    is DetailUiState.Error -> {
                        shimmerDetailContainer.stopShimmer()
                        shimmerDetailContainer.visibility = View.GONE
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is DetailUiState.Success -> {
                        shimmerDetailContainer.stopShimmer()
                        shimmerDetailContainer.visibility = View.GONE
                        response.data.let { detailResult ->
                            detailImageView.load(detailResult.thumbnail) {
                                crossfade(true)
                                placeholder(R.drawable.game_placeholder)
                            }

                            detailDesc.makeCollapsible(3, Int.MAX_VALUE)

                            detailTitle.text = detailResult.title
                            detailGenre.text = detailResult.genre
                            detailPlatform.text = detailResult.platform
                            detailReleaseDate.text =
                                detailResult.releaseDate?.extractYearFromDateString()
                            detailDesc.text = detailResult.description

                            if (detailResult.minimumSystemRequirements == null) {
                                systemReqConstraintLayout.visibility = View.GONE
                                viewLineAbout.visibility = View.GONE
                                viewLineSystemReq.visibility = View.GONE
                            }
                            systemReqOS.text = detailResult.minimumSystemRequirements?.os
                            systemReqCPU.text =
                                detailResult.minimumSystemRequirements?.processor ?: ""
                            systemReqRAM.text = detailResult.minimumSystemRequirements?.memory ?: ""
                            systemReqStorage.text =
                                detailResult.minimumSystemRequirements?.storage ?: ""
                            systemReqGraphics.text =
                                detailResult.minimumSystemRequirements?.graphics ?: ""

                            screenshotRecyclerView.adapter = reviewAdapter
                            reviewAdapter.submitList(detailResult.screenshots ?: emptyList())

                            if (detailResult.screenshots.isNullOrEmpty()) {
                                screenshotTitle.visibility = View.GONE
                            }

                            nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->

                                if (scrollY >= detailTitle.top + detailTitle.height) {
                                    // Detay başlığı görünmüyorsa, toolbar başlığını ayarlayın

                                    detailToolbar.title = detailTitle.text
                                } else {
                                    // Detay başlığı görünüyorsa, toolbar başlığını boş bırakın
                                    detailToolbar.title = ""
                                }
                            })


                        }
                    }

                }
            }
        }
    }
}

