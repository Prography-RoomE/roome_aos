package com.sevenstars.roome.view.profile.horror

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.model.profile.info.HorrorThemePositions
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileHorrorBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.VerticalSpaceItemDecoration
import com.sevenstars.roome.view.profile.hint.ProfileHintFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileHorrorFragment: BaseFragment<FragmentProfileHorrorBinding>(R.layout.fragment_profile_horror) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileHorrorViewModel by viewModels()
    private lateinit var horrorAdapter: ProfileHorrorRvAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(6)

        horrorAdapter = ProfileHorrorRvAdapter().apply {
            this.setItemClickListener(object : ProfileHorrorRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean, item: HorrorThemePositions) {
                    if(isChecked) viewModel.saveData(item.id)
                }
            })
        }

        binding.rvHorror.apply {
            adapter = horrorAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
            addItemDecoration(VerticalSpaceItemDecoration(requireContext(), 12))
        }

        horrorAdapter.setData(profileViewModel.profileDefaultData.horrorThemePositions)
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
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileHintFragment(), null)
                    viewModel.setLoadingState()
                }
            }
        }
    }
}