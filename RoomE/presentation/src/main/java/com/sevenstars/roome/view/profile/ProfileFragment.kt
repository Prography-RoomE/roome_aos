package com.sevenstars.roome.view.profile

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels
import com.sevenstars.roome.R
import com.sevenstars.roome.base.BaseFragment
import com.sevenstars.roome.databinding.FragmentProfileBinding
import com.sevenstars.roome.exetnsion.setColorBackground

class ProfileFragment: BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun initView() {
        (requireActivity() as ProfileActivity).setToolbarVisibility(false)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottieProfile.pauseAnimation()
            binding.groupLoading.visibility = View.GONE
            binding.groupShow.visibility = View.VISIBLE
        }, 2000)

        setColorBackground(
            binding.ivProfile,
            mode = "gradient",
            shape = "linear",
            orientation = "topLeftToBottomRight",
            startColor = "#FF453C",
            endColor = "#FFACB3",
            isRoundCorner = false
        )
    }

    override fun initListener() {
        super.initListener()
        with(binding){

            tgForward.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    tgVertical.isChecked = false
                    setAspectRatio("1:1")
                }
            }

            tgVertical.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    tgForward.isChecked = false
                    setAspectRatio("3:4")
                }
            }
        }
    }

    private fun setAspectRatio(ratio: String){
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clProfile)

        if (ratio == "3:4") {
            constraintSet.constrainWidth(binding.ivProfile.id, (binding.ivProfile.height/4)*3)
            constraintSet.constrainHeight(binding.ivProfile.id, binding.ivProfile.height)
        } else {
            constraintSet.constrainWidth(binding.ivProfile.id, MATCH_PARENT)
        }

        constraintSet.applyTo(binding.clProfile)
    }
}