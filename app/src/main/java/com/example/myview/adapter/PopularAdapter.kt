package com.example.myview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myview.Popular
import com.example.myview.R

class PopularAdapter(private val categoryList: List<Popular>) :
    RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    class PopularViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Double-check that R.id.txtCategoryName matches the actual ID of the TextView inside your item_category.xml
        val txtTagName: TextView = view.findViewById(R.id.category_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        // Connects to your individual item layout XML file
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.popularlay, parent, false)
        return PopularViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val category = categoryList[position]

        // Binds the name string from the API to your TextView
        holder.txtTagName.text = category.name
    }

    override fun getItemCount(): Int = categoryList.size
}