package com.sevenstars.roome.view.profile.important

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.ImportantFactors
import com.sevenstars.roome.databinding.ItemChipBinding

class ProfileImportantRvAdapter: RecyclerView.Adapter<ProfileImportantRvAdapter.ImportantViewHolder>() {
    private var dataList = listOf<ImportantFactors>()
    var checked = ArrayDeque<ImportantFactors>(3)

    inner class ImportantViewHolder(private val binding: ItemChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ImportantFactors) {
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

        private fun toggleCheckedState(data: ImportantFactors) {
            val wasChecked = checked.contains(data)
            if (wasChecked) {
                checked.remove(data)
                binding.tbChipName.isChecked = false
            } else {
                if (checked.size == 3) {
                    val firstChecked = checked.removeFirst()
                    notifyItemChanged(dataList.indexOf(firstChecked))
                }
                checked.add(data)
                binding.tbChipName.isChecked = true
            }

            itemClickListener.onClick()
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
    fun setData(newList: List<ImportantFactors>){
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
