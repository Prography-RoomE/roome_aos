package com.sevenstars.roome.view.signup

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentSignupNickBinding
import com.sevenstars.roome.utils.UiState
import java.util.regex.Pattern


class SignupNickFragment: BaseFragment<FragmentSignupNickBinding>(R.layout.fragment_signup_nick) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnNext.setOnClickListener {
            if(it.alpha == 1f){
                // 약관 동의 및 닉네임 저장
            }
        }

        binding.btnClear.setOnClickListener {
            binding.etNickname.text.clear()
        }

        binding.etNickname.apply {
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    val length = p0.toString().length

                    binding.btnClear.visibility = if(length != 0) View.VISIBLE else View.GONE
                    viewModel.resetCheckNick()
                }
            })

            filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                val ps: Pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
                if (source == "" || ps.matcher(source).matches()) return@InputFilter source
                ""
            }, InputFilter.LengthFilter(8))
        }

        binding.btnNicknameCheck.setOnClickListener {
            viewModel.checkNick()
        }
    }

    override fun observer() {
        super.observer()

        viewModel.checkState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.error(it.message)

                    binding.btnNext.apply {
                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.neutral10))
                        alpha = 0.12f
                    }
                }
                is UiState.Loading -> {
                    binding.btnNext.apply {
                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.neutral10))
                        alpha = 0.12f
                    }
                }
                is UiState.Success -> {
                    binding.btnNext.apply {
                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_primary))
                        alpha = 1f
                    }
                }
            }
        }
    }
}