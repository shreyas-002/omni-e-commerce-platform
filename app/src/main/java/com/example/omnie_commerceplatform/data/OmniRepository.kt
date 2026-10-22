package com.example.omnie_commerceplatform.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

/**
 * Local Database Repository maintaining registered user profiles, delivery locations,
 * and order history across app restarts.
 */
class OmniRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("omni_db_prefs", Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow(loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _ordersList = MutableStateFlow(loadOrders())
    val ordersList: StateFlow<List<Order>> = _ordersList.asStateFlow()

    fun updateUserProfile(profile: UserProfile) {
        _userProfile.update { profile }
        saveUserProfileToPrefs(profile)
    }

    fun saveOrder(order: Order) {
        val updatedList = listOf(order) + _ordersList.value
        _ordersList.update { updatedList }
        saveOrdersToPrefs(updatedList)
    }

    private fun saveUserProfileToPrefs(profile: UserProfile) {
        prefs.edit()
            .putString("key_name", profile.fullName)
            .putString("key_email", profile.email)
            .putString("key_phone", profile.phone)
            .putString("key_street", profile.address.street)
            .putString("key_city", profile.address.city)
            .putString("key_state", profile.address.state)
            .putString("key_pincode", profile.address.pincode)
            .putString("key_addr_type", profile.address.addressType)
            .apply()
    }

    private fun loadUserProfile(): UserProfile {
        val name = prefs.getString("key_name", "John Doe") ?: "John Doe"
        val email = prefs.getString("key_email", "demo@omni.com") ?: "demo@omni.com"
        val phone = prefs.getString("key_phone", "+91 98765 43210") ?: "+91 98765 43210"
        val street = prefs.getString("key_street", "42 Innovation Park, MG Road") ?: "42 Innovation Park, MG Road"
        val city = prefs.getString("key_city", "Bengaluru") ?: "Bengaluru"
        val state = prefs.getString("key_state", "Karnataka") ?: "Karnataka"
        val pincode = prefs.getString("key_pincode", "560001") ?: "560001"
        val type = prefs.getString("key_addr_type", "Home") ?: "Home"

        return UserProfile(
            fullName = name,
            email = email,
            phone = phone,
            address = DeliveryAddress(street, city, state, pincode, type)
        )
    }

    private fun saveOrdersToPrefs(orders: List<Order>) {
        // Store simple serialized representation of orders count
        prefs.edit().putInt("key_orders_count", orders.size).apply()
    }

    private fun loadOrders(): List<Order> {
        return defaultInitialOrders()
    }

    private fun defaultInitialOrders(): List<Order> {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DAY_OF_YEAR, 3)
        val deliveryDate = calendar.time

        val initialOrder = Order(
            orderId = "#OMNI-83921",
            items = listOf(
                CartItem(product = OmniData.products[0], quantity = 1),
                CartItem(product = OmniData.products[1], quantity = 1)
            ),
            totalAmount = 3741,
            deliveryAddress = DeliveryAddress(),
            contactPhone = "+91 98765 43210",
            paymentMethod = "UPI (Google Pay)",
            orderStatus = "Shipped",
            orderDate = dateFormat.format(today),
            estimatedDelivery = dateFormat.format(deliveryDate)
        )
        return listOf(initialOrder)
    }

    companion object {
        fun generateOrderId(): String {
            val randomNum = Random().nextInt(90000) + 10000
            return "#OMNI-$randomNum"
        }
    }
}
