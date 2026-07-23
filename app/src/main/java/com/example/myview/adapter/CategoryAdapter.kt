package com.example.myview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myview.databinding.ItemCategoryBinding

data class Category(
    val name: String,
    val iconResId: Int // e.g., R.drawable.mobile_icon
)

class CategoryAdapter(private val categoryList: List <Category> ): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {
        holder.bind((categoryList[position]))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding ): RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category){
            binding.categoryImage.setImageResource(category.iconResId)
            binding.categoryName.text=category.name
        }
    }

}
