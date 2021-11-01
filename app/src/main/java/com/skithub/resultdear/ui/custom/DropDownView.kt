package com.skithub.resultdear.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatTextView


class DropDownView: AppCompatTextView, View.OnClickListener {

    private var options: ArrayList<String> = ArrayList()

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        this.setOnClickListener(this)
    }

    private fun popupWindowsort(context: Context): PopupWindow? {
        val popupWindow = PopupWindow(context)
        popupWindow.width = this.width
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context, android.R.layout.simple_dropdown_item_1line,
            options
        )
        val listViewSort = ListView(context)
        listViewSort.adapter = adapter
        listViewSort.setOnItemClickListener { parent, view, position, id ->
            this.text = options[position]
            popupWindow.dismiss()
        }

        // some other visual settings for popup window
        popupWindow.isFocusable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.contentView = listViewSort
        return popupWindow
    }




    override fun onClick(p0: View?) {
        p0?.let {
            if (it === this) {
                val window = popupWindowsort(it.getContext())
                window!!.showAsDropDown(it, 0, 0)
            }
        }
    }

    fun setOptions(options: ArrayList<String>) {
        this.options = options
    }


}