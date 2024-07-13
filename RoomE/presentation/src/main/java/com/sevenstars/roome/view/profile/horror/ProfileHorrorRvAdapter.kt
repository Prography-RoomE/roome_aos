package com.sevenstars.roome.view.profile.horror

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.data.utils.LoggerUtils
import com.sevenstars.domain.model.profile.info.HorrorThemePositions
import com.sevenstars.roome.databinding.CustomToggleButtonBinding

class ProfileHorrorRvAdapter: RecyclerView.Adapter<ProfileHorrorRvAdapter.HorrorViewHolder>() {
    private var dataList = listOf<HorrorThemePositions>()
    var checked: HorrorThemePositions? = null

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

                    isChecked = (checked?.id == data.id)
                    setOnClickListener { toggleCheckedState(data) }
                }
            }
        }

        private fun toggleCheckedState(data: HorrorThemePositions){
            val wasChecked = (checked?.id == data.id)
            if (wasChecked) {
                checked = null
                binding.toggleButton.isChecked = false
            } else {
                checked = data
                binding.toggleButton.isChecked = true
            }
            itemClickListener.onClick(isFull = checked != null)
            notifyDataSetChanged()
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

    fun checkedItem(horrorThemePositions: HorrorThemePositions){
        checked = horrorThemePositions
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onClick(isFull: Boolean)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}
