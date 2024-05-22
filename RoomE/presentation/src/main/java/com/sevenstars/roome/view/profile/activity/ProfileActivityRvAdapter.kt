package com.sevenstars.roome.view.profile.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.Activities
import com.sevenstars.domain.model.profile.info.HintUsagePreferences
import com.sevenstars.domain.model.profile.info.HorrorThemePositions
import com.sevenstars.domain.model.profile.info.ImportantFactors
import com.sevenstars.roome.databinding.CustomToggleButtonBinding
import com.sevenstars.roome.databinding.ItemChipBinding

class ProfileActivityRvAdapter: RecyclerView.Adapter<ProfileActivityRvAdapter.ActivityViewHolder>() {
    private var dataList = listOf<Activities>()

    inner class ActivityViewHolder(private val binding: CustomToggleButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Activities) {
            binding.apply {
                tvMbti.text = data.title
                tvMbtiDesc.text = data.description
                toggleButton.apply {
                    text = null
                    textOn = null
                    textOff = null

                    setOnCheckedChangeListener { _, isChecked ->
                        itemClickListener.onClick(isChecked)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = CustomToggleButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Activities>){
        dataList = newList
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onClick(isChecked: Boolean)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}
