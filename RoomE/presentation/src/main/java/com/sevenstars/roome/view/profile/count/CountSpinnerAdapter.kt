package com.sevenstars.roome.view.profile.count

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.sevenstars.domain.model.profile.info.CountRange
import com.sevenstars.roome.databinding.ItemSpinnerBinding

class CountSpinnerAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    val values: List<CountRange>
) : ArrayAdapter<CountRange>(context, resId, values) {

    override fun getCount(): Int = values.size

    override fun getItem(position: Int): CountRange = values[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        try {
            binding.tvSpinnerItem.text = values[position].title
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        try {
            binding.tvSpinnerItem.text = values[position].title
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    fun selectItemContaining(countTitle: String): Int {
        if (countTitle == "선택") return 0

        for ((index, item) in values.withIndex()) {
            if (item.title == "${countTitle}번") {
                return index
            } else if (item.title == "301번 이상" && countTitle == "301~"){
                return index
            }
        }
        return -1
    }
}
