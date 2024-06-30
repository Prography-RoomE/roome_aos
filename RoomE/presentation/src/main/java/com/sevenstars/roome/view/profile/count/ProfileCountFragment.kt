package com.sevenstars.roome.view.profile.count

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.model.profile.info.CountRange
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileCountBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.genres.ProfileGenresFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCountFragment : BaseFragment<FragmentProfileCountBinding>(R.layout.fragment_profile_count) {
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
        binding.etCount.setSelection(binding.etCount.length() - 1)

        // Ensure at least one toggle is checked initially
        if (!binding.tgCountRange.isChecked && !binding.tgCountDirectly.isChecked) {
            binding.tgCountDirectly.isChecked = true
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            if (binding.tgCountRange.isChecked) {
                viewModel.saveRangeCountData(spinnerAdapter.getItem(binding.spinnerCount.selectedItemPosition))
            } else {
                viewModel.saveCountData(binding.etCount.text.toString().replace("번", "").toInt(), false)
            }
        }

        binding.btnNextDirect.setOnClickListener {
            if (binding.tgCountRange.isChecked) {
                viewModel.saveRangeCountData(spinnerAdapter.getItem(binding.spinnerCount.selectedItemPosition))
            } else {
                viewModel.saveCountData(binding.etCount.text.toString().replace("번", "").toInt(), false)
            }
        }

        binding.tgCountRange.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tgCountDirectly.isChecked = false
                binding.etCount.visibility = View.GONE
                binding.spinnerCount.visibility = View.VISIBLE
                binding.ivToggle.visibility = View.VISIBLE
                hideKeyboard()
            } else if (!binding.tgCountDirectly.isChecked) {
                binding.tgCountRange.isChecked = true
            }
        }

        binding.tgCountDirectly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tgCountRange.isChecked = false
                binding.etCount.visibility = View.VISIBLE
                binding.spinnerCount.visibility = View.GONE
                binding.ivToggle.visibility = View.GONE
                showKeyboardAndFocus(binding.etCount)
            } else if (!binding.tgCountRange.isChecked) {
                binding.tgCountDirectly.isChecked = true
            }
        }

        binding.etCount.apply {
            setOnClickListener {
                if (binding.etCount.text.isNotEmpty()) binding.etCount.setSelection(binding.etCount.text.length - 1)
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        binding.etCount.removeTextChangedListener(this)

                        val text = s.toString().replace("번", "")
                        if (text.isNotEmpty()) {
                            binding.etCount.setText("${text.toInt()}번")
                            binding.etCount.setSelection(binding.etCount.text.length - 1)
                        } else {
                            binding.etCount.setText("0번")
                            binding.etCount.setSelection(binding.etCount.text.length - 1)
                        }

                        binding.etCount.addTextChangedListener(this)
                    }
                }
            })
        }
    }

    override fun observer() {
        super.observer()

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    LoggerUtils.error("저장 실패\n${it.message}")
                    showToast("저장 실패\n${it.message}")
                    if (it.code == 0) showNoConnectionDialog(R.id.fl_profile)
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    profileViewModel.selectedProfileData.count = binding.etCount.text.toString().replace("번", "").toInt()
                    (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileGenresFragment(), null)
                    viewModel.setLoadingState()
                }
            }
        }
    }

    private fun setSpinner() {
        val tmp = profileViewModel.profileDefaultData.roomCountRanges.toMutableList().apply {
            add(0, CountRange(0, 0, 0, "기타"))
        }
        spinnerAdapter = CountSpinnerAdapter(requireContext(), R.layout.item_spinner, tmp)
        binding.spinnerCount.dropDownVerticalOffset = 20
        binding.spinnerCount.adapter = spinnerAdapter
        binding.spinnerCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.btnNext.apply {
                    if (spinnerAdapter.values[position].id == 0) {
                        backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.btn_disabled)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_a12))
                    } else {
                        backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.primary_primary)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onKeyboardVisibilityChanged(isVisible: Boolean) {
        if (binding.etCount.hasFocus()) {
            binding.btnNextDirect.visibility = View.VISIBLE
            binding.btnNext.visibility = View.GONE
        } else {
            try {
                binding.etCount.clearFocus()
                binding.btnNextDirect.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE
            } catch (e: NullPointerException) {
                LoggerUtils.error(e.message.toString())
            }
        }
    }
}