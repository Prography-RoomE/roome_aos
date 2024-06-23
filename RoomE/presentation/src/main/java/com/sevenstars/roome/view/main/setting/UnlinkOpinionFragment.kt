package com.sevenstars.roome.view.main.setting

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentUnlinkOpinionBinding

class UnlinkOpinionFragment: BaseFragment<FragmentUnlinkOpinionBinding>(R.layout.fragment_unlink_opinion) {

    override fun initView() {}

    override fun initListener() {
        super.initListener()

        binding.btnNextStep.setOnClickListener {
            if(binding.btnNextStep.currentTextColor == ContextCompat.getColor(requireContext(), R.color.surface)){
                moveNext()
            }
        }

//        binding.etUnlinkOpinion.setOnFocusChangeListener { _, hasFocus ->
//            if(hasFocus){
//                binding.btnCheck.visibility = View.VISIBLE
//                binding.btnNextStep.visibility = View.GONE
//            }
//        }

        binding.btnCheck.setOnClickListener {
            hideKeyboard()
            binding.etUnlinkOpinion.clearFocus()
            binding.btnCheck.visibility = View.GONE
            binding.btnNextStep.visibility = View.VISIBLE
        }

        binding.etUnlinkOpinion.apply {
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if(s.isNullOrEmpty()){
                        binding.btnNextStep.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.on_surface_a12)
                        binding.btnNextStep.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant))
                    } else {
                        binding.btnNextStep.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), R.color.primary_primary)
                        binding.btnNextStep.setTextColor(ContextCompat.getColor(requireContext(), R.color.surface))
                    }
                }
            })
        }

        binding.apply {
            tbUnlink.btnBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
            tbUnlink.btnNext.setOnClickListener { moveNext() }
        }
    }

    private fun moveNext(){
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, UnlinkCheckFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onKeyboardVisibilityChanged(isVisible: Boolean) {
        if (isVisible && binding.etUnlinkOpinion.hasFocus()) {
            binding.btnCheck.visibility = View.VISIBLE
            binding.btnNextStep.visibility = View.GONE
        }
        else {
            try {
                binding.etUnlinkOpinion.clearFocus()
                binding.btnCheck.visibility = View.GONE
                binding.btnNextStep.visibility = View.VISIBLE
            } catch (e: NullPointerException){
                LoggerUtils.error(e.message.toString()) // 임시 방편
            }
        }
    }
}
