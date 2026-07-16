package com.example.myview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myview.databinding.EachItemBinding

// 1. Correctly declare CarouselViewHolder as the adapter type target parameter
class CarouselAdapter(private val imageList: MutableList<Int>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    // 2. Fixed 'Binding.root' case typo to 'binding.root'
    inner class CarouselViewHolder(private val binding: EachItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Int) {
            binding.imageView.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
//        return CarouselViewHolder(EachItemBinding.inflate.from(parent.context),parent,false)    }
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = EachItemBinding.inflate(layoutInflater, parent, false)
    return CarouselViewHolder(binding)
}
    override fun getItemCount(): Int {
        return imageList.size

    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        // 5. Connect the list data object directly to your inner view binder logic
        holder.bind(imageList[position])
        // 6. Added the missing item counter method required by RecyclerView

    }
}