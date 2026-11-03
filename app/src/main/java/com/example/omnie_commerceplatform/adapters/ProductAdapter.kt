package com.example.omnie_commerceplatform.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.omnie_commerceplatform.data.Product
import com.example.omnie_commerceplatform.databinding.ItemProductCardBinding
import java.util.Locale

class ProductAdapter(
    private var products: List<Product>,
    private var cartProductIds: Set<String> = emptySet(),
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val isAdded = cartProductIds.contains(product.id)

        holder.binding.apply {
            tvBadgeCategory.text = product.category
            tvBrandTagline.text = product.brandTagline
            tvProductTitle.text = product.title
            tvProductPrice.text = "₹${String.format(Locale.US, "%,d", product.priceInRupees)}"
            tvRating.text = "${product.rating} (${product.reviewCount})"

            if (isAdded) {
                btnAddToCart.text = "✓ Added to Cart"
                btnAddToCart.setBackgroundColor(Color.parseColor("#E6F4EA"))
                btnAddToCart.setTextColor(Color.parseColor("#137333"))
            } else {
                btnAddToCart.text = "+ Add to Cart"
                btnAddToCart.setBackgroundColor(Color.parseColor("#F8F9FA"))
                btnAddToCart.setTextColor(Color.parseColor("#1C1D21"))
            }

            imgProduct.load(product.imageUrl.ifBlank { product.drawableRes }) {
                crossfade(true)
                error(product.drawableRes)
                placeholder(product.drawableRes)
            }

            btnAddToCart.setOnClickListener { onAddToCart(product) }
        }
    }

    override fun getItemCount(): Int = products.size

    fun submitList(newList: List<Product>, newCartIds: Set<String> = cartProductIds) {
        products = newList
        cartProductIds = newCartIds
        notifyDataSetChanged()
    }
}
