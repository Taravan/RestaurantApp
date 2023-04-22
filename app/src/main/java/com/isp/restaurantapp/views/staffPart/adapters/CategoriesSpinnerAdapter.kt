package com.isp.restaurantapp.views.staffPart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.isp.restaurantapp.models.dto.CategoryDTO

class CategoriesSpinnerAdapter(context: Context, categories: List<CategoryDTO>):
    ArrayAdapter<CategoryDTO>(context, android.R.layout.simple_spinner_item, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)
        view.findViewById<TextView>(android.R.id.text1).text = getItem(position)?.name ?: ""

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        view.findViewById<TextView>(android.R.id.text1).text = getItem(position)?.name ?: ""

        return view
    }

    override fun getItem(position: Int): CategoryDTO? {
        return super.getItem(position)
    }

    override fun getPosition(item: CategoryDTO?): Int {
        return super.getPosition(item)
    }

}