package com.example.myview.adapter
import com.google.android.material.tabs.TabLayoutMediator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myview.R
import com.example.myview.databinding.FeaturedInsideBinding
class InsideFeaturedAdapter (
    private val images: List<Any>
) : RecyclerView.Adapter<InsideFeaturedAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: FeaturedInsideBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = FeaturedInsideBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

//    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        Glide.with(holder.itemView.context)
//            .load(images[position])
//            .into(holder.binding.imageView)
//    }
override fun onBindViewHolder(holder: ImageViewHolder, position: Int) { Glide.with(holder.itemView.context) .load(images[position]) .placeholder(
    R.drawable.resource_default) .fitCenter() .into(holder.binding.imageView) // KEEP THIS!
    }

    override fun getItemCount() = images.size
}
