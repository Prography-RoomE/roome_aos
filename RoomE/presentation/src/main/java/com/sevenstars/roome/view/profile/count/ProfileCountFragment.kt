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
import com.sevenstars.domain.enums.ProfileState
import com.sevenstars.domain.model.profile.info.CountRange
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileCountBinding
import com.sevenstars.roome.utils.AnalyticsHelper
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import com.sevenstars.roome.view.profile.ProfileActivity
import com.sevenstars.roome.view.profile.ProfileViewModel
import com.sevenstars.roome.view.profile.genres.ProfileGenresFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCountFragment(
    private val roomCount: String = "기타"
) : BaseFragment<FragmentProfileCountBinding>(R.layout.fragment_profile_count) {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val viewModel: ProfileCountViewModel by viewModels()
    private lateinit var spinnerAdapter: CountSpinnerAdapter

    override fun initView() {
        AnalyticsHelper.logScreenView("number")
        if (isProfileActivity()) {
            setupProfileActivity()
        } else {
            setupMainActivity()
        }
    }

    private fun isProfileActivity() = requireActivity().localClassName == "view.profile.ProfileActivity"

    private fun setupProfileActivity() {
        (requireActivity() as ProfileActivity).apply {
            setToolbarVisibility(true)
            setStep(1)
        }
        setView()
    }

    private fun setupMainActivity() {
        (requireActivity() as MainActivity).apply {
            setBottomNaviVisibility(false)
            setToolbarVisibility(true)
        }
        profileViewModel.fetchDefaultData(ProfileState.COMPLETE)
        setupNextButtonForMainActivity()
        binding.tvComment.visibility = View.GONE
    }

    private fun setupNextButtonForMainActivity() {
        binding.btnNext.apply {
            setText(R.string.btn_save)
            backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.primary_primary)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
        }
        binding.btnNextDirect.setText(R.string.btn_save)
    }

    private fun setView() {
        setSpinner()
        setRoomCount()

//        binding.etCount.setText("${profileViewModel.selectedProfileData.count}번")
//        binding.etCount.setSelection(binding.etCount.length() - 1)
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            saveCountData()
        }

        binding.btnNextDirect.setOnClickListener {
            saveCountData()
        }

        setupToggleButtons()
        setupEditText()
    }

    private fun saveCountData() {
        if (binding.tgCountRange.isChecked && (binding.spinnerCount.selectedItem as CountRange).title != "기타") {
            viewModel.saveRangeCountData(spinnerAdapter.getItem(binding.spinnerCount.selectedItemPosition))
        } else {
            val count = binding.etCount.text.toString().replace("번", "")
            viewModel.saveCountData(if(count.isEmpty()) 0 else count.toInt(), false)
        }
    }

    private fun setupToggleButtons() {
        binding.tgCountRange.isChecked = true
        binding.tgCountDirectly.isChecked = false

        binding.tgCountRange.setOnCheckedChangeListener { _, isChecked ->
            handleRangeToggleChecked(isChecked)
        }

        binding.tgCountDirectly.setOnCheckedChangeListener { _, isChecked ->
            handleDirectToggleChecked(isChecked)
        }
    }

    private fun handleRangeToggleChecked(isChecked: Boolean) {
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

    private fun handleDirectToggleChecked(isChecked: Boolean) {
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

    private fun setupEditText() {
        binding.etCount.apply {
            setOnClickListener {
                if (text.isNotEmpty()) setSelection(text.length - 1)
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
            handleUiState(it)
        }

        if (requireActivity().localClassName == "view.main.MainActivity") {
            profileViewModel.profileDataState.observe(this) {
                handleProfileDataState(it)
            }
        }
    }

    private fun handleUiState(uiState: UiState<Unit>) {
        when (uiState) {
            is UiState.Failure -> {
                LoggerUtils.error("저장 실패\n${uiState.message}")
                showToast("저장 실패\n${uiState.message}")
                if (uiState.code == 0) showNoConnectionDialog(R.id.fl_profile)
            }
            is UiState.Loading -> {}
            is UiState.Success -> {
                handleSuccessState()
                viewModel.setLoadingState()
            }
        }
    }

    private fun handleSuccessState() {
        AnalyticsHelper.logButtonClick("number_next")

        if (isProfileActivity()) {
            val count = binding.etCount.text.toString().replace("번", "")
            profileViewModel.selectedProfileData.count = if(count.isEmpty()) 0 else count.toInt()
            (requireActivity() as ProfileActivity).replaceFragmentWithStack(ProfileGenresFragment(), null)
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun handleProfileDataState(profileDataState: UiState<ProfileState>) {
        when (profileDataState) {
            is UiState.Failure -> {
                LoggerUtils.error("프로필 생성에 필요한 데이터 조회 실패\n${profileDataState.message}")
                showToast("프로필 생성에 필요한 데이터 조회 실패")
            }
            is UiState.Loading -> {}
            is UiState.Success -> {
                setView()
            }
        }
    }

    private fun setSpinner() {
        val countRanges = profileViewModel.profileDefaultData.roomCountRanges.toMutableList().apply {
            add(0, CountRange(0, 0, 0, "기타"))
        }
        spinnerAdapter = CountSpinnerAdapter(requireContext(), R.layout.item_spinner, countRanges)
        binding.spinnerCount.dropDownVerticalOffset = 20
        binding.spinnerCount.adapter = spinnerAdapter
        binding.spinnerCount.onItemSelectedListener = createSpinnerItemSelectedListener()
    }

    private fun createSpinnerItemSelectedListener() = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (isProfileActivity()) {
                updateNextButtonAppearance(position)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    private fun updateNextButtonAppearance(position: Int) {
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

    override fun onKeyboardVisibilityChanged(isVisible: Boolean) {
        if (binding.etCount.hasFocus()) {
            binding.btnNextDirect.visibility = View.VISIBLE
            binding.btnNext.visibility = View.GONE
        } else {
            clearFocusAndUpdateButtonVisibility()
        }
    }

    private fun clearFocusAndUpdateButtonVisibility() {
        try {
            binding.etCount.clearFocus()
            binding.btnNextDirect.visibility = View.GONE
            binding.btnNext.visibility = View.VISIBLE
        } catch (e: NullPointerException) {
            LoggerUtils.error(e.message.toString())
        }
    }

    private fun setRoomCount() {
        val roomCountRanges = profileViewModel.profileDefaultData.roomCountRanges
        if (roomCountRanges.any { it.title.contains("~") || it.title.contains("이상") || it.title.contains("기타") }) {
            val index = spinnerAdapter.selectItemContaining(roomCount)
            if (index == -1) {
                binding.tgCountDirectly.performClick()
                binding.etCount.setText(roomCount)
            } else {
                binding.spinnerCount.setSelection(index)
            }
        } else {
            binding.etCount.setText(roomCount)
        }
    }
}
