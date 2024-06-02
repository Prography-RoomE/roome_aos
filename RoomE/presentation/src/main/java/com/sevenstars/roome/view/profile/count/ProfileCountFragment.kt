package com.sevenstars.roome.view.profile.count

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication
import com.sevenstars.roome.databinding.FragmentProfileCountBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.genres.ProfileGenresFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCountFragment: BaseFragment<FragmentProfileCountBinding>(R.layout.fragment_profile_count) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileCountViewModel by viewModels()
    private lateinit var spinnerAdapter: CountSpinnerAdapter

    override fun initView() {
        (requireActivity() as ProfileActivity).apply {
            setToolbarVisibility(true)
            setStep(1)
        }
        setSpinner()
        binding.etCount.setText("${profileViewModel.selectedProfileData.count}번")
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            viewModel.saveData(binding.etCount.text.toString().replace("번", "").toInt(), false)
//            if(binding.tgCountRange.isChecked){
//
//            }
        }

        binding.tgCountRange.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.tgCountDirectly.isChecked = false
                binding.etCount.visibility = View.GONE
                binding.spinnerCount.visibility = View.VISIBLE
            }
        }

        binding.tgCountDirectly.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.tgCountRange.isChecked = false
                binding.etCount.visibility = View.VISIBLE
                binding.spinnerCount.visibility = View.GONE
            }
        }

        binding.etCount.apply {
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && !s.toString().endsWith("번")) {
                        binding.etCount.removeTextChangedListener(this)
                        val text = s.toString().replace("번", "")
                        binding.etCount.setText("${text}번")
                        binding.etCount.setSelection(text.length)
                        binding.etCount.addTextChangedListener(this)
                    }
                }
            })
        }
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
                    profileViewModel.selectedProfileData.count = binding.etCount.text.toString().replace("번", "").toInt()
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileGenresFragment())
                    viewModel.setLoadingState()
                }
            }
        }
    }

    private fun setSpinner(){
        spinnerAdapter = CountSpinnerAdapter(requireContext(), R.layout.item_spinner, profileViewModel.profileDefaultData.roomCountRanges)
        binding.spinnerCount.dropDownVerticalOffset = 20
        binding.spinnerCount.adapter = spinnerAdapter
    }
}