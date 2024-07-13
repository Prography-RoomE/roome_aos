package com.sevenstars.roome.view.profile.color

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sevenstars.domain.model.profile.info.Colors
import com.sevenstars.roome.databinding.ItemColorChipBinding
import com.sevenstars.roome.exetnsion.setColorBackground


class ProfileColorRvAdapter: RecyclerView.Adapter<ProfileColorRvAdapter.ColorViewHolder>() {
    private var dataList = listOf<Colors>()
    var checked: Colors? = null

    inner class ColorViewHolder(private val binding: ItemColorChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Colors) {
            setColorBackground(
                view = binding.btnColor,
                mode = data.mode,
                shape = data.shape,
                orientation = data.direction,
                startColor = data.startColor,
                endColor = data.endColor,
                isRoundCorner = true,
                hasStroke = true,
                strokeValue = if(checked?.id == data.id) 4f else 8f,
                strokeColor = if(checked?.id == data.id) "#FF3344" else "#52FFFFFF"
            )

            binding.btnColor.setOnClickListener { toggleCheckedState(data) }
        }

        private fun toggleCheckedState(data: Colors){
            val wasChecked = (checked?.id == data.id)
            if (wasChecked) {
                checked = null
            } else {
                checked = data
            }
            itemClickListener.onClick(isFull = checked != null)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemColorChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Colors>){
        dataList = newList
        notifyDataSetChanged()
    }

    fun checkedItem(color: Colors){
        checked = color
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
