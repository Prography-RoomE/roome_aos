package com.sevenstars.roome.view.main.profile.edit

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import com.sevenstars.roome.databinding.FragmentUserProfileEditBinding
import com.sevenstars.roome.utils.UiState
import com.sevenstars.roome.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class UserProfileEditFragment(private val nickname: String): BaseFragment<FragmentUserProfileEditBinding>(R.layout.fragment_user_profile_edit) {
    private val viewModel: UserProfileEditViewModel by viewModels()

    override fun initView() {
        (requireActivity() as MainActivity).setBottomNaviVisibility(false)
        binding.btnClear.visibility = View.VISIBLE
        binding.etNickname.setText(nickname)
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnNext.setOnClickListener {
            viewModel.checkNick(binding.etNickname.text.toString())
        }

        binding.btnClear.setOnClickListener {
            binding.etNickname.text.clear()
        }

        binding.etNickname.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    val length = p0.toString().length

                    binding.btnClear.visibility = if (length != 0) View.VISIBLE else View.GONE
//                    binding.btnNext.apply {
//                        if (length >= 2) {
//                            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_primary))
//                            alpha = 1f
//                        } else {
//                            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.neutral10))
//                            alpha = 0.12f
//                        }
//                    }

                    binding.tvNickname.setTextColor(requireContext().getColor(R.color.on_surface))
                    binding.tvNicknameFeedback.apply {
                        text = getText(R.string.signup_nickname_default)
                        setTextColor(requireContext().getColor(R.color.on_surface))
                    }
                }
            })

            filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                val ps: Pattern =
                    Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
                if (source == "" || ps.matcher(source).matches()) return@InputFilter source
                ""
            }, InputFilter.LengthFilter(8))
        }
    }

    override fun observer() {
        super.observer()

        viewModel.checkState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error(it.message)
                    if(it.code == 0) showNoConnectionDialog(R.id.fl_signup)

                    binding.tvNickname.setTextColor(requireContext().getColor(R.color.error))
                    binding.btnClear.visibility = View.VISIBLE
                    binding.btnClear.setImageResource(R.drawable.ic_error)
                    binding.tvNicknameFeedback.apply {
                        text = it.message
                        setTextColor(requireContext().getColor(R.color.error))
                    }

//                    binding.btnNext.apply {
//                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.neutral10))
//                        alpha = 0.12f
//                    }
                }
                is UiState.Loading -> {
//                    binding.btnNext.apply {
//                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.neutral10))
//                        alpha = 0.12f
//                    }
                }
                is UiState.Success -> {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }
}