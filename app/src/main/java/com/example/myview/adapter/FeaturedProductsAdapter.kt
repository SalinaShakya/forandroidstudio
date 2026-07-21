package com.example.myview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myview.ProductResponse
import com.example.myview.databinding.FeaturedProductsBinding

class FeaturedProductsAdapter(
    private val products: List<ProductResponse>
) : RecyclerView.Adapter<FeaturedProductsAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: FeaturedProductsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding = FeaturedProductsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = products[position]

        holder.binding.txtTitle.text = product.title
        holder.binding.txtCategory.text = product.category
        holder.binding.txtPrice.text = "Rs. ${product.price}"

        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.binding.imgProduct)
    }

    override fun getItemCount(): Int = products.size
}