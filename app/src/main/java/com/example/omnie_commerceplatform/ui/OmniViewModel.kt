package com.example.omnie_commerceplatform.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.omnie_commerceplatform.data.CartItem
import com.example.omnie_commerceplatform.data.DeliveryAddress
import com.example.omnie_commerceplatform.data.DemoAccount
import com.example.omnie_commerceplatform.data.OmniData
import com.example.omnie_commerceplatform.data.OmniRepository
import com.example.omnie_commerceplatform.data.Order
import com.example.omnie_commerceplatform.data.PincodeLookup
import com.example.omnie_commerceplatform.data.Product
import com.example.omnie_commerceplatform.data.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isSignUpMode: Boolean = false,
    val fullName: String = "",
    val email: String = "",
    val pass: String = "",
    val phone: String = "",
    val streetAddress: String = "",
    val city: String = "",
    val state: String = "",
    val pincode: String = "",
    val isPasswordVisible: Boolean = false,
    val rememberMe: Boolean = true,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

data class ShopUiState(
    val selectedCategory: String = "All products",
    val searchQuery: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val isCartOpen: Boolean = false,
    val isCheckoutOpen: Boolean = false
)

class OmniViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OmniRepository(application.applicationContext)

    val userProfileFlow: StateFlow<UserProfile> = repository.userProfile
    val ordersListFlow: StateFlow<List<Order>> = repository.ordersList

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    private val _shopUiState = MutableStateFlow(ShopUiState())
    val shopUiState: StateFlow<ShopUiState> = _shopUiState.asStateFlow()

    val filteredProductsFlow: StateFlow<List<Product>> = _shopUiState
        .map { state ->
            val query = state.searchQuery.trim()
            val category = state.selectedCategory

            OmniData.products.filter { product ->
                val matchesCategory = (category == "All products") ||
                        product.category.equals(category, ignoreCase = true)

                val matchesSearch = query.isBlank() ||
                        product.title.contains(query, ignoreCase = true) ||
                        product.category.contains(query, ignoreCase = true) ||
                        product.brandTagline.contains(query, ignoreCase = true)

                matchesCategory && matchesSearch
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OmniData.products
        )

    fun toggleAuthMode() {
        _authUiState.update { it.copy(isSignUpMode = !it.isSignUpMode, errorMessage = null) }
    }

    fun onFullNameChange(name: String) {
        _authUiState.update { it.copy(fullName = name, errorMessage = null) }
    }

    fun onEmailChange(newEmail: String) {
        _authUiState.update { it.copy(email = newEmail, errorMessage = null) }
    }

    fun onPasswordChange(newPass: String) {
        _authUiState.update { it.copy(pass = newPass, errorMessage = null) }
    }

    fun onPhoneChange(phone: String) {
        _authUiState.update { it.copy(phone = phone, errorMessage = null) }
    }

    fun onStreetChange(street: String) {
        _authUiState.update { it.copy(streetAddress = street, errorMessage = null) }
    }

    fun onCityChange(city: String) {
        _authUiState.update { current ->
            val suggestedPin = PincodeLookup.lookupPincode(city)
            current.copy(
                city = city,
                pincode = suggestedPin ?: current.pincode,
                errorMessage = null
            )
        }
    }

    fun onStateChange(state: String) {
        _authUiState.update { it.copy(state = state, errorMessage = null) }
    }

    fun onPincodeChange(pincode: String) {
        _authUiState.update { current ->
            var updatedCity = current.city
            var updatedState = current.state

            if (pincode.length >= 2) {
                val loc = PincodeLookup.lookupLocation(pincode)
                if (loc != null) {
                    updatedCity = loc.city
                    updatedState = loc.state
                }
            }

            current.copy(
                pincode = pincode,
                city = updatedCity,
                state = updatedState,
                errorMessage = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _authUiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleRememberMe() {
        _authUiState.update { it.copy(rememberMe = !it.rememberMe) }
    }

    fun selectDemoAccount(demo: DemoAccount) {
        _authUiState.update {
            it.copy(
                email = demo.email,
                pass = demo.pass,
                errorMessage = null
            )
        }
    }

    fun signIn() {
        val current = _authUiState.value
        val cleanEmail = current.email.trim()

        if (cleanEmail.isBlank()) {
            _authUiState.update { it.copy(errorMessage = "Please enter an email address.") }
            return
        }

        if (!cleanEmail.contains("@") || !cleanEmail.contains(".")) {
            _authUiState.update { it.copy(errorMessage = "Please enter a valid email address.") }
            return
        }

        if (current.pass.length < 6) {
            _authUiState.update { it.copy(errorMessage = "Password must be at least 6 characters.") }
            return
        }

        val updatedProfile = UserProfile(
            fullName = if (current.isSignUpMode) current.fullName else repository.userProfile.value.fullName,
            email = cleanEmail,
            phone = current.phone,
            address = DeliveryAddress(current.streetAddress, current.city, current.state, current.pincode)
        )
        repository.updateUserProfile(updatedProfile)

        _authUiState.update {
            it.copy(
                successMessage = if (current.isSignUpMode) "Account created successfully! Welcome to Omni." else "Signed in successfully. Welcome back to Omni!",
                errorMessage = null,
                isLoggedIn = true
            )
        }
    }

    fun signOut() {
        _authUiState.update {
            it.copy(
                isLoggedIn = false,
                successMessage = null,
                errorMessage = null
            )
        }
    }

    fun onCategorySelected(category: String) {
        _shopUiState.update { it.copy(selectedCategory = category) }
    }

    fun onSearchQueryChanged(query: String) {
        _shopUiState.update { it.copy(searchQuery = query) }
    }

    fun resetFilters() {
        _shopUiState.update {
            it.copy(
                selectedCategory = "All products",
                searchQuery = ""
            )
        }
    }

    fun addToCart(product: Product) {
        _shopUiState.update { current ->
            val existing = current.cartItems.find { it.product.id == product.id }
            val updatedList = if (existing != null) {
                current.cartItems.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1)
                    else it
                }
            } else {
                current.cartItems + CartItem(product = product, quantity = 1)
            }
            current.copy(cartItems = updatedList, isCartOpen = true)
        }
    }

    fun updateCartQuantity(productId: String, delta: Int) {
        _shopUiState.update { current ->
            val updatedList = current.cartItems.mapNotNull { item ->
                if (item.product.id == productId) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else {
                    item
                }
            }
            current.copy(cartItems = updatedList)
        }
    }

    fun toggleCartOpen(isOpen: Boolean) {
        _shopUiState.update { it.copy(isCartOpen = isOpen) }
    }

    fun toggleCheckoutOpen(isOpen: Boolean) {
        _shopUiState.update { it.copy(isCheckoutOpen = isOpen) }
    }

    fun clearCart() {
        _shopUiState.update { it.copy(cartItems = emptyList()) }
    }

    fun placeOrder(address: DeliveryAddress, phone: String, paymentMethod: String): Order {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DAY_OF_YEAR, 3)
        val deliveryDate = calendar.time

        val items = _shopUiState.value.cartItems
        val total = items.sumOf { it.product.priceInRupees * it.quantity }

        val newOrder = Order(
            orderId = OmniRepository.generateOrderId(),
            items = items,
            totalAmount = total,
            deliveryAddress = address,
            contactPhone = phone,
            paymentMethod = paymentMethod,
            orderStatus = "Confirmed",
            orderDate = dateFormat.format(today),
            estimatedDelivery = dateFormat.format(deliveryDate)
        )

        repository.saveOrder(newOrder)
        clearCart()

        return newOrder
    }
}
