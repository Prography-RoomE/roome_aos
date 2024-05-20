package com.sevenstars.roome.view.profile.genres

import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileGenresBinding
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.ProfileWelcomeFragment

class ProfileGenresFragment: BaseFragment<FragmentProfileGenresBinding>(R.layout.fragment_profile_genres) {
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var genresAdapter: ProfileGenresRvAdapter

    override fun initView() {
        genresAdapter = ProfileGenresRvAdapter().apply {
            this.setItemClickListener(object : ProfileGenresRvAdapter.OnItemClickListener{
                override fun onClick() {
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
            })
        }

        binding.rvGenres.apply {
            adapter = genresAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(SpaceItemDecoration(requireContext(), 6))
        }

        genresAdapter.setData(viewModel.profileData.genres)
    }

    override fun observer() {
        super.observer()
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileWelcomeFragment())
            LoggerUtils.info(genresAdapter.checked.joinToString(", "))
        }
    }

}