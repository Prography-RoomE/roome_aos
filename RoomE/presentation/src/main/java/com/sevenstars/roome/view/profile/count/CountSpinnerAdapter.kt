package com.sevenstars.roome.view.profile.count

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.sevenstars.roome.databinding.ItemSpinnerBinding

class CountSpinnerAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val values: List<String>
): ArrayAdapter<String>(context, resId, values) {

    override fun getCount(): Int =values.size

    override fun getItem(position: Int): String? = values[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        try {
            binding.tvSpinnerItem.text = values[position]
        } catch (e: Exception){
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        try {
            binding.tvSpinnerItem.text = values[position]
        } catch (e: Exception){
            e.printStackTrace()
        }
        return binding.root
    }
}