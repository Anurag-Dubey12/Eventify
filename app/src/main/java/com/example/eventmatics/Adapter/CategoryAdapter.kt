package com.example.eventmatics.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.eventmatics.R
import com.example.eventmatics.data_class.SpinnerItem

class CategoryAdapter(context: Context, items: List<SpinnerItem>) :
    ArrayAdapter<SpinnerItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.budgetcategory, parent, false)

        val item = getItem(position)

        val imageView: ImageView = view.findViewById(R.id.category_icon)
        val textView: TextView = view.findViewById(R.id.category_name)

        imageView.setImageResource(item?.imageres ?: R.drawable.home) // Set a default image resource
        textView.text = item?.text

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent) // Reuse the getView() method
    }
}
