package com.sevenstars.roome.view.profile.important

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.ImportantFactors
import com.sevenstars.roome.databinding.ItemChipBinding

class ProfileImportantRvAdapter: RecyclerView.Adapter<ProfileImportantRvAdapter.ImportantViewHolder>() {
    private var dataList = listOf<ImportantFactors>()
    var checked = mutableListOf<ImportantFactors>()

    inner class ImportantViewHolder(private val binding: ItemChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ImportantFactors) {
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

        private fun toggleCheckedState(data: ImportantFactors) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImportantViewHolder {
        val binding = ItemChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImportantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImportantViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setCheckedItem(importantFactors: List<ImportantFactors>){
        checked.addAll(importantFactors)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<ImportantFactors>){
        dataList = newList
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
