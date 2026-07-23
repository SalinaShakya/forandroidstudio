package com.example.myview.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myview.ProductResponse
import com.example.myview.R



    class PopularBrandAdapter(private val productList: List<ProductResponse>) :
        RecyclerView.Adapter<PopularBrandAdapter.BrandViewHolder>() {

        class BrandViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
            val txtTitle: TextView = view.findViewById(R.id.txtTitle)
            val txtCategory: TextView = view.findViewById(R.id.txtCategory)
            val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
            // 🟢 Inflates your brand new layout file instead of the shared one
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.popularbrand, parent, false)
            return BrandViewHolder(view)
        }

        override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
            val product = productList[position]

            holder.txtTitle.text = product.title
            holder.txtCategory.text = product.category
            holder.txtPrice.text = "Rs ${product.price}"

            Glide.with(holder.itemView.context)
                .load(product.image)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgProduct)
        }

        override fun getItemCount(): Int = productList.size
    }

