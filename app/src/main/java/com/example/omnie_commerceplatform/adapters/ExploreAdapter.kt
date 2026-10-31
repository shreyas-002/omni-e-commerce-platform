package com.example.omnie_commerceplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.omnie_commerceplatform.data.Product
import com.example.omnie_commerceplatform.databinding.ItemExploreCategoryBinding

data class DepartmentCategory(
    val name: String,
    val countText: String,
    val sampleProduct: Product
)

class ExploreAdapter(
    private val departments: List<DepartmentCategory>,
    private val onDepartmentClick: (String) -> Unit
) : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {

    inner class ExploreViewHolder(val binding: ItemExploreCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val binding = ItemExploreCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val dept = departments[position]

        holder.binding.apply {
            tvDepartmentName.text = dept.name
            tvDepartmentCount.text = dept.countText

            imgDepartment.load(dept.sampleProduct.imageUrl.ifBlank { dept.sampleProduct.drawableRes }) {
                crossfade(true)
                error(dept.sampleProduct.drawableRes)
                placeholder(dept.sampleProduct.drawableRes)
            }

            root.setOnClickListener {
                onDepartmentClick(dept.name)
            }
        }
    }

    override fun getItemCount(): Int = departments.size
}
