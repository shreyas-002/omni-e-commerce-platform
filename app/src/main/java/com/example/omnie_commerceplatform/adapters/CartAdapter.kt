package com.example.omnie_commerceplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.omnie_commerceplatform.data.CartItem
import com.example.omnie_commerceplatform.databinding.ItemCartRowBinding
import java.util.Locale

class CartAdapter(
    private var items: List<CartItem>,
    private val onQuantityChange: (productId: String, delta: Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        holder.binding.apply {
            tvCartTitle.text = item.product.title
            tvCartPrice.text = "₹${String.format(Locale.US, "%,d", item.product.priceInRupees)}"
            tvQty.text = item.quantity.toString()

            imgCartItem.load(item.product.imageUrl.ifBlank { item.product.drawableRes }) {
                crossfade(true)
                error(item.product.drawableRes)
                placeholder(item.product.drawableRes)
            }

            btnDecrease.setOnClickListener { onQuantityChange(item.product.id, -1) }
            btnIncrease.setOnClickListener { onQuantityChange(item.product.id, 1) }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newList: List<CartItem>) {
        items = newList
        notifyDataSetChanged()
    }
}
