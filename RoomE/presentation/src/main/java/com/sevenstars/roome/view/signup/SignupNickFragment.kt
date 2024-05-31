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
import com.sevenstars.roome.base.RoomeApplication.Companion.userName
import com.sevenstars.roome.databinding.FragmentSignupNickBinding
import com.sevenstars.roome.utils.UiState
import java.util.regex.Pattern


class SignupNickFragment: BaseFragment<FragmentSignupNickBinding>(R.layout.fragment_signup_nick) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun initView() {}

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnNext.setOnClickListener {
            if(it.alpha == 1f){
//                viewModel.saveTermsAgreement()
                viewModel.checkNick(binding.etNickname.text.toString())
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
                    binding.btnNext.apply {
                        if(length >= 2) {
                            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_primary))
                            alpha = 1f
                        } else {
                            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.neutral10))
                            alpha = 0.12f
                        }
                    }

                    binding.tvNickname.setTextColor(requireContext().getColor(R.color.on_surface))
                    binding.btnClear.setImageResource(R.drawable.ic_clear)
                    binding.tvNicknameFeedback.apply {
                        text = getText(R.string.signup_nickname_default)
                        setTextColor(requireContext().getColor(R.color.on_surface))
                    }
                }
            })

            filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                val ps: Pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$")
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

                    binding.tvNickname.setTextColor(requireContext().getColor(R.color.error))
                    binding.btnClear.setImageResource(R.drawable.ic_error)
                    binding.tvNicknameFeedback.apply {
                        text = it.message
                        setTextColor(requireContext().getColor(R.color.error))
                    }

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
                    viewModel.saveTermsAgreement()
//                    binding.tvNicknameFeedback.apply {
//                        text = "사용 가능한 닉네임입니다."
//                        setTextColor(requireContext().getColor(R.color.success))
//                    }

//                    binding.btnNext.apply {
//                        setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_primary))
//                        alpha = 1f
//                    }
                }
            }
        }

        viewModel.saveState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Failure -> {
                    LoggerUtils.error(it.message)
                    showToast(it.message)
                }
                is UiState.Loading -> {}
                is UiState.Success -> {
                    LoggerUtils.debug("회원가입 성공")
                    userName = binding.etNickname.text.toString()
                    (requireActivity() as SignUpActivity).moveToProfile()
                }
            }
        }
    }
}