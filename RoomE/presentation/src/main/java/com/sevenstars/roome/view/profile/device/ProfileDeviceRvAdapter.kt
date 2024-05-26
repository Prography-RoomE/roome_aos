package com.sevenstars.roome.view.profile.device

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.DeviceLockPreferences
import com.sevenstars.roome.databinding.ItemChipBinding

class ProfileDeviceRvAdapter: RecyclerView.Adapter<ProfileDeviceRvAdapter.DeviceViewHolder>() {
    private var dataList = listOf<DeviceLockPreferences>()

    inner class DeviceViewHolder(private val binding: ItemChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DeviceLockPreferences) {
            binding.tbChipName.apply {
                text = data.title
                textOn = data.title
                textOff = data.title

                setOnCheckedChangeListener { _, isChecked ->
                    itemClickListener.onClick(isChecked, data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<DeviceLockPreferences>){
        dataList = newList
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onClick(isChecked: Boolean, data: DeviceLockPreferences)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}
