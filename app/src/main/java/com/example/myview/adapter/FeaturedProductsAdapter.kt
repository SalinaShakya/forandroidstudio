package com.example.myview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myview.data.model.ProductResponse
import com.example.myview.databinding.FeaturedProductsBinding
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.myview.CartActivity
import com.example.myview.FeaturedActivity
import com.example.myview.MainActivity
import com.example.myview.data.CartManager
import com.example.myview.data.FavoriteManager
import com.example.myview.data.local.FavoriteEntity
import com.google.android.material.snackbar.Snackbar

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

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FeaturedActivity::class.java)
            intent.putExtra("product_id", product.id) 
            holder.itemView.context.startActivity(intent)
        }
        //expand
        holder.binding.btnAdd.setOnClickListener {

            product.quantity = 1
            CartManager.addToCart(product) //update data then refresh ui
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

                CartManager.updateQuantity(product.id, product.quantity)

            } else {

                product.quantity = 0

                CartManager.removeFromCart(product.id)

                notifyItemChanged(holder.bindingAdapterPosition)
            }
        }
        //increase
        holder.binding.btnPlus.setOnClickListener {
            product.quantity++
            holder.binding.txtQuantity.text = product.quantity.toString()
            CartManager.updateQuantity(product.id, product.quantity)
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
        holder.binding.btnFavourite.setOnClickListener {
            val favorite = FavoriteEntity(
                id = product.id,
                title = product.title,
                price = product.price,
                image = product.image
            )
            FavoriteManager.addFavorite(favorite)
            Toast.makeText(holder.itemView.context, "Added to favorites", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int = products.size
}