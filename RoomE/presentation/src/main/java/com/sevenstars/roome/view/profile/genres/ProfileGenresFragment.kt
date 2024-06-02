package com.sevenstars.roome.view.profile.genres

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileGenresBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.SpaceItemDecoration
import com.sevenstars.roome.view.profile.mbti.ProfileMbtiFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileGenresFragment: BaseFragment<FragmentProfileGenresBinding>(R.layout.fragment_profile_genres) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileGenresViewModel by viewModels()
    private lateinit var genresAdapter: ProfileGenresRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(2)

        genresAdapter = ProfileGenresRvAdapter().apply {
            this.setItemClickListener(object : ProfileGenresRvAdapter.OnItemClickListener{
                override fun onClick(isFull: Boolean) {
                    if(isFull) showToast("최대 2개까지 선택할 수 있어요.")
                    setNextBtn()
                }
            })
        }

        binding.rvGenres.apply {
            genresAdapter.checked.addAll(profileViewModel.selectedProfileData.genres)
            setNextBtn()
            adapter = genresAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(SpaceItemDecoration(requireContext(), 6))
        }

        genresAdapter.setData(profileViewModel.profileDefaultData.genres)
    }

    override fun observer() {
        super.observer()

        viewModel.uiState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error("저장 실패\n${it.message}")
                    showToast("저장 실패\n${it.message}")
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    profileViewModel.selectedProfileData.genres = genresAdapter.checked
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileMbtiFragment(), null)
                    viewModel.setLoadingState()
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            LoggerUtils.info(genresAdapter.checked.joinToString(", "))
            if(binding.btnNext.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
                viewModel.saveData(genresAdapter.checked.map { it.id })
            }
        }
    }

    private fun setNextBtn(){
        binding.btnNext.apply {
            if(genresAdapter.checked.isNotEmpty()){
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_primary)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
            } else {
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.btn_disabled)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
            }
        }
    }

}