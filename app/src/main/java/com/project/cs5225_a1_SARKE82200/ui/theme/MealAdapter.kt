package com.project.cs5225_a1_SARKE82200.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.cs5225_a1_SARKE82200.model.MealSummary
import com.project.cs5225_a1_SARKE82200.databinding.ItemMealBinding

class MealAdapter(
    private var items: List<MealSummary>,
    private val onClick: (MealSummary) -> Unit
) : RecyclerView.Adapter<MealAdapter.VH>() {

    inner class VH(val b: ItemMealBinding) : RecyclerView.ViewHolder(b.root) {
        init {
            b.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) onClick(items[pos])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val m = items[position]
        holder.b.txtMealName.text = m.strMeal ?: "Unknown"
        holder.b.txtMealCategory.text = ""
        holder.b.txtMealArea.text = ""
        Glide.with(holder.b.imgThumb.context)
            .load(m.strMealThumb)
            .centerCrop()
            .into(holder.b.imgThumb)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<MealSummary>) {
        items = newItems
        notifyDataSetChanged()
    }
}
