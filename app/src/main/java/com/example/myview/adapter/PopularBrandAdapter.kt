package com.example.myview.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myview.FeaturedActivity
import com.example.myview.MainActivity
import com.example.myview.R
import com.example.myview.data.model.ProductResponse
import com.example.myview.databinding.PopularbrandBinding
import com.google.android.material.snackbar.Snackbar

class PopularBrandAdapter(
    private val productList: List<ProductResponse>
) : RecyclerView.Adapter<PopularBrandAdapter.BrandViewHolder>() {

    class BrandViewHolder(val binding: PopularbrandBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding = PopularbrandBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val product = productList[position]

        holder.binding.txtTitle.text = product.title
        holder.binding.txtCategory.text = product.category
        holder.binding.txtPrice.text = "Rs ${product.price}"

        Glide.with(holder.itemView.context)
            .load(product.image)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.binding.imgProduct)

        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, FeaturedActivity::class.java)

            intent.putExtra("title", product.title)
            intent.putExtra("category", product.category)
            intent.putExtra("price", product.price)
            intent.putExtra("description", product.description)
            intent.putExtra("image", product.image)

            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FeaturedActivity::class.java)
            holder.itemView.context.startActivity(intent)

        }
        //expand
        holder.binding.btnAdd.setOnClickListener {

            product.quantity = 1
            notifyItemChanged(holder.bindingAdapterPosition)

            Snackbar.make(
                holder.binding.root,
                "Added to cart successfully.",
                Snackbar.LENGTH_LONG
            )
                .setAction("GOTO CART") {

                    val intent = Intent(
                        holder.itemView.context,
                        MainActivity::class.java
                    )

                    intent.putExtra("open_fragment", "cart")

                    holder.itemView.context.startActivity(intent)

                }
                .show()
        }
        //collapse
        holder.binding.btnMinus.setOnClickListener {
            if (product.quantity > 1) {
                product.quantity--
                holder.binding.txtQuantity.text = product.quantity.toString()
            } else {
                product.quantity = 0
                notifyItemChanged(holder.bindingAdapterPosition)
            }
        }
        //increase
        holder.binding.btnPlus.setOnClickListener {
            product.quantity++
            holder.binding.txtQuantity.text = product.quantity.toString()
        }
        //ui
        if (product.quantity == 0) {

            holder.binding.btnAdd.visibility = View.VISIBLE
            holder.binding.quantityCard.visibility = View.GONE

        } else {

            holder.binding.btnAdd.visibility = View.GONE
            holder.binding.quantityCard.visibility = View.VISIBLE
            holder.binding.txtQuantity.text = product.quantity.toString()

        }
    }

    override fun getItemCount(): Int = productList.size
}