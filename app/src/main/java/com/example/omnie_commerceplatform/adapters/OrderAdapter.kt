package com.example.omnie_commerceplatform.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omnie_commerceplatform.data.Order
import com.example.omnie_commerceplatform.databinding.ItemOrderCardBinding
import java.util.Locale

class OrderAdapter(
    private var orders: List<Order>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: ItemOrderCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.binding.apply {
            tvOrderId.text = order.orderId
            tvOrderStatus.text = order.orderStatus
            tvOrderDate.text = "Placed: ${order.orderDate}"
            tvOrderItems.text = order.items.joinToString("\n") { "${it.quantity}x ${it.product.title}" }
            tvOrderTotal.text = "Total Paid: ₹${String.format(Locale.US, "%,d", order.totalAmount)} (${order.paymentMethod})"
        }
    }

    override fun getItemCount(): Int = orders.size

    fun submitList(newList: List<Order>) {
        orders = newList
        notifyDataSetChanged()
    }
}
