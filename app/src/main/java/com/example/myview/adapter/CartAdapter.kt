package com.example.myview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myview.data.CartManager
import com.example.myview.data.model.CartItem
import com.example.myview.databinding.FragmentCartBinding
import com.example.myview.databinding.ItemSelectorBinding

//no need for the CartManager or the FragmentCart now
//the adapter is also somehow only responsible for the ui ig
//no math
//now the fragment observes the cartItems list from the ViewModel and whenever the changes occur
//the fragment tells the adapter to refresh
//
class CartAdapter(
    private val cartItems: MutableList<CartItem>,
//    private val onCartUpdate: (() -> Unit)? = null // Callback to update totals in the Fragment (this was when no ViewModel)
    private val onPlusClick: (CartItem) -> Unit, // Callback for plus button
    private val onMinusClick: (CartItem) -> Unit // Callback for minus button
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemSelectorBinding)
        : RecyclerView.ViewHolder(binding.root)

    //this oonCreateViewHolder is same
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

        //till here same
        // PLUS for this what we do is just remove all the logic

        holder.binding.btnPlus.setOnClickListener {
//            val currentPos = holder.bindingAdapterPosition
//            if (currentPos != RecyclerView.NO_POSITION) {
//                item.quantity++
//                holder.binding.txtQuantity.text = item.quantity.toString()
//
//                // Update the data manager
//                CartManager.updateQuantity(item.id, item.quantity)
//
//                // Trigger callback to update total price in UI
//                onCartUpdate?.invoke()
//            }
            onPlusClick(item) //add this
        }

        // MINUS same as the plus thing remove the logic ig
        holder.binding.btnMinus.setOnClickListener {
//            val currentPos = holder.bindingAdapterPosition
//            if (currentPos == RecyclerView.NO_POSITION) return@setOnClickListener
//
//            if (item.quantity > 1) {
//                item.quantity--
//                holder.binding.txtQuantity.text = item.quantity.toString()
//                CartManager.updateQuantity(item.id, item.quantity)
//            } else {
//                // Remove item completely if quantity reaches 0
//                CartManager.removeFromCart(item.id)
////                cartItems.removeAt(currentPos)
//                notifyItemRemoved(currentPos)
//                notifyItemRangeChanged(currentPos, cartItems.size)
//            }
//
//            // Trigger callback to update total price in UI
//            onCartUpdate?.invoke()
            onMinusClick(item)
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
