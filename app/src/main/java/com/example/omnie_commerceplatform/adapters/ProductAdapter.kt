package com.example.omnie_commerceplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.omnie_commerceplatform.data.Product
import com.example.omnie_commerceplatform.databinding.ItemProductCardBinding
import java.util.Locale

class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.binding.apply {
            tvBadgeCategory.text = product.category
            tvBrandTagline.text = product.brandTagline
            tvProductTitle.text = product.title
            tvProductPrice.text = "₹${String.format(Locale.US, "%,d", product.priceInRupees)}"
            tvRating.text = "${product.rating} (${product.reviewCount})"

            imgProduct.load(product.imageUrl.ifBlank { product.drawableRes }) {
                crossfade(true)
                error(product.drawableRes)
                placeholder(product.drawableRes)
            }

            btnAddToCart.setOnClickListener { onAddToCart(product) }
        }
    }

    override fun getItemCount(): Int = products.size

    fun submitList(newList: List<Product>) {
        products = newList
        notifyDataSetChanged()
    }
}
