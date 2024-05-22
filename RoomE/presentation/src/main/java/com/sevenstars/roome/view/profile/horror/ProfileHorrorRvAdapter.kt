package com.sevenstars.roome.view.profile.horror

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.HorrorThemePositions
import com.sevenstars.domain.model.profile.info.ImportantFactors
import com.sevenstars.roome.databinding.CustomToggleButtonBinding
import com.sevenstars.roome.databinding.ItemChipBinding

class ProfileHorrorRvAdapter: RecyclerView.Adapter<ProfileHorrorRvAdapter.HorrorViewHolder>() {
    private var dataList = listOf<HorrorThemePositions>()

    inner class HorrorViewHolder(private val binding: CustomToggleButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HorrorThemePositions) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorrorViewHolder {
        val binding = CustomToggleButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HorrorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HorrorViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<HorrorThemePositions>){
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
