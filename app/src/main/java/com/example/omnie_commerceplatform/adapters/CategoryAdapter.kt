package com.example.omnie_commerceplatform.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omnie_commerceplatform.databinding.ItemCategoryPillBinding

class CategoryAdapter(
    private val categories: List<String>,
    private var selectedCategory: String,
    private val onCategorySelected: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryPillBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryPillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        val isSelected = category == selectedCategory

        holder.binding.tvCategoryName.text = category
        holder.binding.tvCategoryName.setTextColor(if (isSelected) Color.WHITE else Color.parseColor("#1C1D21"))
        holder.binding.root.setOnClickListener {
            selectedCategory = category
            notifyDataSetChanged()
            onCategorySelected(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateSelection(category: String) {
        selectedCategory = category
        notifyDataSetChanged()
    }
}
