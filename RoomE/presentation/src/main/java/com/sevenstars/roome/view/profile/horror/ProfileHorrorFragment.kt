package com.sevenstars.roome.view.profile.horror

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileHorrorBinding
import com.sevenstars.roome.databinding.FragmentProfileImportantFactorBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.ProfileWelcomeFragment
import com.sevenstars.roome.view.profile.VerticalSpaceItemDecoration
import com.sevenstars.roome.view.profile.hint.ProfileHintFragment

class ProfileHorrorFragment: BaseFragment<FragmentProfileHorrorBinding>(R.layout.fragment_profile_horror) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var horrorAdapter: ProfileHorrorRvAdapter


    override fun initView() {
        (requireActivity() as ProfileActivity).setStep(6)

        horrorAdapter = ProfileHorrorRvAdapter().apply {
            this.setItemClickListener(object : ProfileHorrorRvAdapter.OnItemClickListener{
                override fun onClick(isChecked: Boolean) {
                    if(isChecked) (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileHintFragment())
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

    override fun initListener() {
        super.initListener()

    }
}