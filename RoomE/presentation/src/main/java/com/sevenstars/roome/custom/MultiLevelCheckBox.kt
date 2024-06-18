package com.sevenstars.roome.custom

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.sevenstars.roome.R

class MultiLevelCheckBox(context: Context, attributeSet: AttributeSet) : AppCompatCheckBox(context, attributeSet) {
    private val parentId: Int
    private val parentCheckBox get() = rootView.findViewById<MultiLevelCheckBox>(parentId)

    private val checkedChildrenId = mutableListOf<Int>()
    private val childrenId = mutableListOf<Int>()
    private val childrenCheckBox get() = childrenId.map { rootView.findViewById<MultiLevelCheckBox>(it) }

    init {
        context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.MultiLevelCheckBox, 0, 0
        ).apply {
            try {
                parentId = getResourceId(R.styleable.MultiLevelCheckBox_parentCheckBox, -1)
            } finally {
                recycle()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        parentCheckBox?.childrenId?.add(id)
    }

    override fun performClick(): Boolean {
        val willBeChecked = !isChecked
        updateChildren(willBeChecked)
        updateParent(willBeChecked)
        return super.performClick()
    }

    private fun updateChildren(checked: Boolean) {
        childrenCheckBox.forEach {
            it.isChecked = checked
            it.updateChildren(checked)
        }
        with(checkedChildrenId) {
            if (checked) {
                clear()
                addAll(childrenId)
            } else {
                clear()
            }
        }
    }

    private fun updateParent(checked: Boolean) {
        parentCheckBox?.notifyChildChange(id, checked)
    }

    private fun notifyChildChange(childCheckBoxId: Int, checked: Boolean) {
        with(checkedChildrenId) {
            if (checked) {
                add(childCheckBoxId)
                takeIf { size == childrenId.size }?.let {
                    isChecked = true
                    updateParent(true)
                }
            } else {
                remove(childCheckBoxId)
                isChecked = false
                updateParent(false)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.checkedChildrenId = checkedChildrenId.toIntArray()
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            checkedChildrenId.clear()
            checkedChildrenId.addAll(state.checkedChildrenId.toList())
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState : BaseSavedState {
        var checkedChildrenId = intArrayOf()

        constructor(superState: Parcelable?) : super(superState)

        private constructor(parcel: Parcel) : super(parcel) {
            checkedChildrenId = parcel.createIntArray() ?: intArrayOf()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeIntArray(checkedChildrenId)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(parcel: Parcel) = SavedState(parcel)
                override fun newArray(size: Int) = arrayOfNulls<SavedState>(size)
            }
        }
    }
}
