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
                isRoundCorner = true
            )

            binding.btnColor.setOnClickListener{
                itemClickListener.onClick(true)
            }
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

    interface OnItemClickListener{
        fun onClick(isChecked: Boolean)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}
