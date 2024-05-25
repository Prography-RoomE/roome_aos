package com.sevenstars.roome.view.profile.strength

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.Strengths
import com.sevenstars.roome.databinding.ItemChipBinding

class ProfileStrengthRvAdapter: RecyclerView.Adapter<ProfileStrengthRvAdapter.StrengthViewHolder>() {
    private var dataList = listOf<Strengths>()
    var checked = ArrayDeque<Strengths>(2)

    inner class StrengthViewHolder(private val binding: ItemChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Strengths) {
            binding.tbChipName.apply {
                text = data.title
                textOn = data.title
                textOff = data.title
                isChecked = checked.contains(data)
                setOnClickListener {
                    toggleCheckedState(data)
                }
            }
        }

        private fun toggleCheckedState(data: Strengths) {
            val wasChecked = checked.contains(data)
            if (wasChecked) {
                checked.remove(data)
                binding.tbChipName.isChecked = false
            } else {
                if (checked.size == 2) {
                    val firstChecked = checked.removeFirst()
                    notifyItemChanged(dataList.indexOf(firstChecked))
                }
                checked.add(data)
                binding.tbChipName.isChecked = true
            }

            itemClickListener.onClick()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrengthViewHolder {
        val binding = ItemChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StrengthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StrengthViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Strengths>){
        dataList = newList
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
