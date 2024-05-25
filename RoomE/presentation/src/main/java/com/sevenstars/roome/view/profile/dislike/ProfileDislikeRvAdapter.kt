package com.sevenstars.roome.view.profile.dislike

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.DislikedFactors
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
                isChecked = checked.contains(data)
                setOnClickListener {
                    toggleCheckedState(data)
                }
            }
        }

        private fun toggleCheckedState(data: DislikedFactors) {
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

    interface OnItemClickListener{
        fun onClick()
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}
