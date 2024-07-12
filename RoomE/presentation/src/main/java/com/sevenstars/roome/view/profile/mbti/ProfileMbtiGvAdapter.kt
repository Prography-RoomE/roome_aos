package com.sevenstars.roome.view.profile.mbti

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.sevenstars.domain.model.profile.mbti.MbtiEntity
import com.sevenstars.roome.R
import com.sevenstars.roome.base.RoomeApplication.Companion.app
import com.sevenstars.roome.databinding.CustomToggleButtonBinding

class ProfileMbtiGvAdapter : BaseAdapter() {
    private val items = listOf(
        MbtiEntity(1, app.getString(R.string.profile_mbti_e), app.getString(R.string.profile_mbti_e_desc)),
        MbtiEntity(1, app.getString(R.string.profile_mbti_i), app.getString(R.string.profile_mbti_i_desc)),
        MbtiEntity(2, app.getString(R.string.profile_mbti_n), app.getString(R.string.profile_mbti_n_desc)),
        MbtiEntity(2, app.getString(R.string.profile_mbti_s), app.getString(R.string.profile_mbti_s_desc)),
        MbtiEntity(3, app.getString(R.string.profile_mbti_t), app.getString(R.string.profile_mbti_t_desc)),
        MbtiEntity(3, app.getString(R.string.profile_mbti_f), app.getString(R.string.profile_mbti_f_desc)),
        MbtiEntity(4, app.getString(R.string.profile_mbti_j), app.getString(R.string.profile_mbti_j_desc)),
        MbtiEntity(4, app.getString(R.string.profile_mbti_p), app.getString(R.string.profile_mbti_p_desc)),
    )

    val checkedItems = mutableMapOf<Int, String>()
    var disabledState = false

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView == null) {
            val tempBinding = CustomToggleButtonBinding.inflate(LayoutInflater.from(parent?.context))
            tempBinding.root.tag = tempBinding
            tempBinding
        } else {
            convertView.tag
        } as CustomToggleButtonBinding

        bind(binding, items[position])

        return binding.root
    }

    private fun bind(binding: CustomToggleButtonBinding, data: MbtiEntity) = with(binding) {
        tvMbti.text = data.name
        tvMbtiDesc.text = data.desc
        toggleButton.apply {
            text = null
            textOn = null
            textOff = null
            isChecked = checkedItems[data.type] == data.name

//            isEnabled = !disabledState

            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkedItems[data.type] = data.name
                    notifyDataSetChanged()
                } else if (checkedItems[data.type] == data.name) {
                    checkedItems.remove(data.type)
                }

                itemClickListener.onClick()
            }
        }


        val alphaValue = if (checkedItems.isEmpty() && disabledState) 0.38f else 1.0f
        tvMbti.alpha = alphaValue
        tvMbtiDesc.alpha = alphaValue

    }

    fun mbtiDisabled(){
        disabledState = true
        checkedItems.clear()
        notifyDataSetChanged()
    }

    fun mbtiActivate(){
        disabledState = false
        notifyDataSetChanged()
    }

    fun setCheckedItems(mbti: String) {
        if(mbti == "-") {
            mbtiDisabled()
            return
        }

        val mbtiList = mbti.toList()

        checkedItems.clear()

        for (char in mbtiList) {
            val matchedItem = items.find { it.name.startsWith(char.toString(), ignoreCase = true) }
            matchedItem?.let {
                checkedItems[it.type] = it.name
            }
        }

        notifyDataSetChanged()
    }


    interface OnItemClickListener{
        fun onClick()
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}
