package com.example.omnie_commerceplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.omnie_commerceplatform.data.Product
import com.example.omnie_commerceplatform.databinding.ItemRecommendedCardBinding
import java.util.Locale

class RecommendedAdapter(
    private var products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<RecommendedAdapter.RecViewHolder>() {

    inner class RecViewHolder(val binding: ItemRecommendedCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val binding = ItemRecommendedCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        val product = products[position]

        holder.binding.apply {
            tvRecTitle.text = product.title
            tvRecPrice.text = "₹${String.format(Locale.US, "%,d", product.priceInRupees)}"

            imgRec.load(product.imageUrl.ifBlank { product.drawableRes }) {
                crossfade(true)
                error(product.drawableRes)
                placeholder(product.drawableRes)
            }

            btnRecAdd.setOnClickListener { onAddToCart(product) }
        }
    }

    override fun getItemCount(): Int = products.size

    fun submitList(newList: List<Product>) {
        products = newList
        notifyDataSetChanged()
    }
}
