package com.sevenstars.roome.view.profile.hint

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.HintUsagePreferences
import com.sevenstars.roome.databinding.CustomToggleButtonBinding

class ProfileHintRvAdapter: RecyclerView.Adapter<ProfileHintRvAdapter.HintViewHolder>() {
    private var dataList = listOf<HintUsagePreferences>()

    inner class HintViewHolder(private val binding: CustomToggleButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HintUsagePreferences) {
            binding.apply {
                tvMbti.text = data.title
                tvMbtiDesc.text = data.description
                toggleButton.apply {
                    text = null
                    textOn = null
                    textOff = null

                    setOnCheckedChangeListener { _, isChecked ->
                        itemClickListener.onClick(isChecked, data)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HintViewHolder {
        val binding = CustomToggleButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HintViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<HintUsagePreferences>){
        dataList = newList
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onClick(isChecked: Boolean, data: HintUsagePreferences)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}
