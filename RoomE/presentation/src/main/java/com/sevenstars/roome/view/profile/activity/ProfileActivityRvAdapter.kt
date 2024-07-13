package com.sevenstars.roome.view.profile.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.Activities
import com.sevenstars.roome.databinding.CustomToggleButtonBinding

class ProfileActivityRvAdapter: RecyclerView.Adapter<ProfileActivityRvAdapter.ActivityViewHolder>() {
    private var dataList = listOf<Activities>()
    var checked: Activities? = null

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

                    isChecked = (checked?.id == data.id)
                    setOnClickListener { toggleCheckedState(data) }
                }
            }
        }

        private fun toggleCheckedState(data: Activities){
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

    fun checkedItem(activities: Activities){
        checked = activities
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
