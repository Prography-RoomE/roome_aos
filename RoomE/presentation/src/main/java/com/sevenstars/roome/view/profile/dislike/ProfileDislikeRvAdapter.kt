package com.sevenstars.roome.view.profile.dislike

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.DislikedFactors
import com.sevenstars.domain.model.profile.info.Genres
import com.sevenstars.roome.databinding.ItemChipBinding

class ProfileDislikeRvAdapter: RecyclerView.Adapter<ProfileDislikeRvAdapter.DislikeViewHolder>() {
    private var dataList = listOf<DislikedFactors>()
    var checked = ArrayDeque<DislikedFactors>(2)

    inner class DislikeViewHolder(private val binding: ItemChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DislikedFactors) {
            binding.tbChipName.apply {
                text = data.title
                textOn = data.title
                textOff = data.title
                isChecked = checked.find { it.id == data.id } != null
                setOnClickListener {
                    toggleCheckedState(data)
                }
            }
        }

        private fun toggleCheckedState(data: DislikedFactors) {
            val wasChecked = checked.find { it.id == data.id } != null
            if (wasChecked) {
                checked.remove(checked.find { it.id == data.id })
                binding.tbChipName.isChecked = false
            } else {
                if (checked.size != 2) {
                    checked.add(data)
                    binding.tbChipName.isChecked = true
                } else {
                    itemClickListener.onClick(isFull = true)
                    binding.tbChipName.isChecked = false
                }
            }

            itemClickListener.onClick(isFull = false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DislikeViewHolder {
        val binding = ItemChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DislikeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DislikeViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<DislikedFactors>){
        dataList = newList
        notifyDataSetChanged()
    }

    fun setChecked(dislikedFactors: List<DislikedFactors>){
        checked.addAll(dislikedFactors)
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
