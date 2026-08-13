package com.example.myview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myview.data.CartManager
import com.example.myview.data.model.CartItem
import com.example.myview.databinding.FragmentCartBinding
import com.example.myview.databinding.ItemSelectorBinding
class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onCartUpdate: (() -> Unit)? = null // Callback to update totals in the Fragment
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemSelectorBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemSelectorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        holder.binding.txtName.text = item.title
        holder.binding.txtPrice.text = "Rs. ${item.price}"
        holder.binding.txtQuantity.text = item.quantity.toString()

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.binding.imgProduct)

        // PLUS
        holder.binding.btnPlus.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                item.quantity++
                holder.binding.txtQuantity.text = item.quantity.toString()
                
                // Update the data manager
                CartManager.updateQuantity(item.id, item.quantity)
                
                // Trigger callback to update total price in UI
                onCartUpdate?.invoke()
            }
        }

        // MINUS
        holder.binding.btnMinus.setOnClickListener {
            val currentPos = holder.bindingAdapterPosition
            if (currentPos == RecyclerView.NO_POSITION) return@setOnClickListener

            if (item.quantity > 1) {
                item.quantity--
                holder.binding.txtQuantity.text = item.quantity.toString()
                CartManager.updateQuantity(item.id, item.quantity)
            } else {
                // Remove item completely if quantity reaches 0
                CartManager.removeFromCart(item.id)
//                cartItems.removeAt(currentPos)
                notifyItemRemoved(currentPos)
                notifyItemRangeChanged(currentPos, cartItems.size)
            }
            
            // Trigger callback to update total price in UI
            onCartUpdate?.invoke()
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
