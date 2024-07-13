package com.sevenstars.roome.view.profile.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass.Device
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.DeviceLockPreferences
import com.sevenstars.domain.model.profile.info.HintUsagePreferences
import com.sevenstars.roome.databinding.CustomToggleButtonBinding

class ProfileDeviceRvAdapter: RecyclerView.Adapter<ProfileDeviceRvAdapter.DeviceViewHolder>() {
    private var dataList = listOf<DeviceLockPreferences>()
    var checked: DeviceLockPreferences? = null

    inner class DeviceViewHolder(private val binding: CustomToggleButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DeviceLockPreferences) {
            binding.tvMbti.text = data.title

            if(data.description.isNullOrEmpty()){
                binding.tvMbtiDesc.visibility = View.GONE
            }
            binding.tvMbtiDesc.text = data.description

            binding.toggleButton.apply {
                text = null
                textOn = null
                textOff = null

                isChecked = (checked?.id == data.id)
                setOnClickListener { toggleCheckedState(data) }
            }
        }

        private fun toggleCheckedState(data: DeviceLockPreferences){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = CustomToggleButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun checkedItem(deviceLockPreferences: DeviceLockPreferences){
        checked = deviceLockPreferences
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
