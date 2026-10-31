package com.example.omnie_commerceplatform

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.omnie_commerceplatform.adapters.CartAdapter
import com.example.omnie_commerceplatform.adapters.CategoryAdapter
import com.example.omnie_commerceplatform.adapters.DepartmentCategory
import com.example.omnie_commerceplatform.adapters.ExploreAdapter
import com.example.omnie_commerceplatform.adapters.OrderAdapter
import com.example.omnie_commerceplatform.adapters.ProductAdapter
import com.example.omnie_commerceplatform.adapters.RecommendedAdapter
import com.example.omnie_commerceplatform.data.DeliveryAddress
import com.example.omnie_commerceplatform.data.OmniData
import com.example.omnie_commerceplatform.data.PincodeLookup
import com.example.omnie_commerceplatform.databinding.ActivityMainBinding
import com.example.omnie_commerceplatform.databinding.DialogCheckoutBinding
import com.example.omnie_commerceplatform.databinding.FragmentCartBinding
import com.example.omnie_commerceplatform.databinding.FragmentExploreBinding
import com.example.omnie_commerceplatform.databinding.FragmentHomeBinding
import com.example.omnie_commerceplatform.databinding.FragmentProfileBinding
import com.example.omnie_commerceplatform.databinding.FragmentSignInBinding
import com.example.omnie_commerceplatform.databinding.FragmentSplashBinding
import com.example.omnie_commerceplatform.ui.OmniViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: OmniViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private var currentTab = R.id.nav_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { view, insets ->
            val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBars.top, 0, 0)
            insets
        }

        setupBottomNavigation()
        showSplashScreen()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            currentTab = item.itemId
            when (item.itemId) {
                R.id.nav_home -> showHomeScreen()
                R.id.nav_explore -> showExploreScreen()
                R.id.nav_cart -> showCartScreen()
                R.id.nav_profile -> showProfileScreen()
            }
            true
        }
    }

    private fun showSplashScreen() {
        binding.bottomNavigation.visibility = View.GONE
        val splashBinding = FragmentSplashBinding.inflate(layoutInflater)
        binding.fragmentContainer.removeAllViews()
        binding.fragmentContainer.addView(splashBinding.root)

        splashBinding.root.postDelayed({
            observeAuthState()
        }, 1200)
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            viewModel.authUiState.collectLatest { authState ->
                if (!authState.isLoggedIn) {
                    binding.bottomNavigation.visibility = View.GONE
                    showSignInScreen()
                } else {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.bottomNavigation.selectedItemId = R.id.nav_home
                    showHomeScreen()
                }
            }
        }
    }

    private fun showSignInScreen() {
        val signInBinding = FragmentSignInBinding.inflate(layoutInflater)
        binding.fragmentContainer.removeAllViews()
        binding.fragmentContainer.addView(signInBinding.root)

        val authState = viewModel.authUiState.value

        signInBinding.apply {
            btnToggleAuth.text = if (authState.isSignUpMode) "Sign In instead" else "Create Account"
            tvAuthTitle.text = if (authState.isSignUpMode) "Join Omni Store" else "Sign in to Omni"
            btnSubmitAuth.text = if (authState.isSignUpMode) "Register & Open Store" else "Sign in"

            val signUpVis = if (authState.isSignUpMode) View.VISIBLE else View.GONE
            etFullName.visibility = signUpVis
            etPhone.visibility = signUpVis
            etStreet.visibility = signUpVis
            etPincode.visibility = signUpVis
            layoutCityState.visibility = signUpVis

            btnToggleAuth.setOnClickListener {
                viewModel.toggleAuthMode()
                showSignInScreen()
            }

            etPincode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val pin = s.toString().trim()
                    if (pin.length >= 2) {
                        val loc = PincodeLookup.lookupLocation(pin)
                        if (loc != null) {
                            etCity.setText(loc.city)
                            etState.setText(loc.state)
                        }
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            val demoList = OmniData.demoAccounts
            if (demoList.size >= 3) {
                demoAccount1.tvDemoLabel.text = demoList[0].label
                demoAccount1.tvDemoEmail.text = demoList[0].email
                demoAccount1.layoutDemoItem.setOnClickListener {
                    viewModel.selectDemoAccount(demoList[0])
                    viewModel.signIn()
                }

                demoAccount2.tvDemoLabel.text = demoList[1].label
                demoAccount2.tvDemoEmail.text = demoList[1].email
                demoAccount2.layoutDemoItem.setOnClickListener {
                    viewModel.selectDemoAccount(demoList[1])
                    viewModel.signIn()
                }

                demoAccount3.tvDemoLabel.text = demoList[2].label
                demoAccount3.tvDemoEmail.text = demoList[2].email
                demoAccount3.layoutDemoItem.setOnClickListener {
                    viewModel.selectDemoAccount(demoList[2])
                    viewModel.signIn()
                }
            }

            btnSubmitAuth.setOnClickListener {
                viewModel.onEmailChange(etEmail.text.toString())
                viewModel.onPasswordChange(etPassword.text.toString())
                if (authState.isSignUpMode) {
                    viewModel.onFullNameChange(etFullName.text.toString())
                    viewModel.onPhoneChange(etPhone.text.toString())
                    viewModel.onStreetChange(etStreet.text.toString())
                    viewModel.onPincodeChange(etPincode.text.toString())
                    viewModel.onCityChange(etCity.text.toString())
                    viewModel.onStateChange(etState.text.toString())
                }
                viewModel.signIn()
            }
        }
    }

    private fun showHomeScreen() {
        val homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        binding.fragmentContainer.removeAllViews()
        binding.fragmentContainer.addView(homeBinding.root)

        val categoryAdapter = CategoryAdapter(
            categories = OmniData.categories,
            selectedCategory = viewModel.shopUiState.value.selectedCategory,
            onCategorySelected = { cat ->
                viewModel.onCategorySelected(cat)
            }
        )

        val productAdapter = ProductAdapter(
            products = viewModel.filteredProductsFlow.value,
            onAddToCart = { product ->
                viewModel.addToCart(product)
                updateCartBadge()
                com.google.android.material.snackbar.Snackbar.make(
                    binding.root,
                    "Added ${product.title} to Cart!",
                    com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                ).setAction("VIEW CART") {
                    binding.bottomNavigation.selectedItemId = R.id.nav_cart
                    showCartScreen()
                }.show()
            }
        )

        homeBinding.rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        homeBinding.rvCategories.adapter = categoryAdapter

        homeBinding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        homeBinding.rvProducts.adapter = productAdapter

        lifecycleScope.launch {
            viewModel.filteredProductsFlow.collectLatest { list ->
                productAdapter.submitList(list)
            }
        }

        homeBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchQueryChanged(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        updateCartBadge()
    }

    private fun showExploreScreen() {
        val exploreBinding = FragmentExploreBinding.inflate(layoutInflater)
        binding.fragmentContainer.removeAllViews()
        binding.fragmentContainer.addView(exploreBinding.root)

        val deptList = OmniData.categories.filter { it != "All products" }.map { cat ->
            val sample = OmniData.products.find { it.category.equals(cat, ignoreCase = true) } ?: OmniData.products[0]
            val count = OmniData.products.count { it.category.equals(cat, ignoreCase = true) }
            DepartmentCategory(cat, "$count Products Available", sample)
        }

        val exploreAdapter = ExploreAdapter(
            departments = deptList,
            onDepartmentClick = { categoryName ->
                viewModel.onCategorySelected(categoryName)
                binding.bottomNavigation.selectedItemId = R.id.nav_home
                showHomeScreen()
            }
        )

        val trendingProducts = OmniData.products.filter { it.rating >= 4.8 }
        val trendingAdapter = ProductAdapter(
            products = trendingProducts,
            onAddToCart = { product ->
                viewModel.addToCart(product)
                updateCartBadge()
                com.google.android.material.snackbar.Snackbar.make(
                    binding.root,
                    "Added ${product.title} to Cart!",
                    com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                ).setAction("VIEW CART") {
                    binding.bottomNavigation.selectedItemId = R.id.nav_cart
                    showCartScreen()
                }.show()
            }
        )

        exploreBinding.rvExploreDepartments.layoutManager = GridLayoutManager(this, 2)
        exploreBinding.rvExploreDepartments.adapter = exploreAdapter

        exploreBinding.rvTrendingProducts.layoutManager = GridLayoutManager(this, 2)
        exploreBinding.rvTrendingProducts.adapter = trendingAdapter

        exploreBinding.etExploreSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchQueryChanged(s.toString())
                binding.bottomNavigation.selectedItemId = R.id.nav_home
                showHomeScreen()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        updateCartBadge()
    }

    private fun showCartScreen() {
        val cartBinding = FragmentCartBinding.inflate(layoutInflater)
        binding.fragmentContainer.removeAllViews()
        binding.fragmentContainer.addView(cartBinding.root)

        val shopState = viewModel.shopUiState.value
        val totalPrice = shopState.cartItems.sumOf { it.product.priceInRupees * it.quantity }
        val isCartEmpty = shopState.cartItems.isEmpty()

        if (isCartEmpty) {
            cartBinding.layoutEmptyCart.visibility = View.VISIBLE
            cartBinding.cardPriceDetails.visibility = View.GONE
            cartBinding.bottomCheckoutBar.visibility = View.GONE
            cartBinding.rvCartItems.visibility = View.GONE
            cartBinding.tvCartHeader.text = "YOUR CART IS EMPTY"

            cartBinding.btnExploreStore.setOnClickListener {
                binding.bottomNavigation.selectedItemId = R.id.nav_home
                showHomeScreen()
            }
        } else {
            cartBinding.layoutEmptyCart.visibility = View.GONE
            cartBinding.cardPriceDetails.visibility = View.VISIBLE
            cartBinding.bottomCheckoutBar.visibility = View.VISIBLE
            cartBinding.rvCartItems.visibility = View.VISIBLE
            cartBinding.tvCartHeader.text = "MY SHOPPING CART (${shopState.cartItems.sumOf { it.quantity }} items)"

            cartBinding.tvCartMrp.text = "₹${String.format(Locale.US, "%,d", totalPrice)}"
            cartBinding.tvCartTotalPayable.text = "₹${String.format(Locale.US, "%,d", totalPrice)}"
        }

        val cartAdapter = CartAdapter(
            items = shopState.cartItems,
            onQuantityChange = { productId, delta ->
                viewModel.updateCartQuantity(productId, delta)
                showCartScreen()
                updateCartBadge()
            }
        )

        val recommendedProducts = OmniData.products.filter { rec ->
            shopState.cartItems.none { it.product.id == rec.id }
        }

        val recAdapter = RecommendedAdapter(
            products = recommendedProducts,
            onAddToCart = { rec ->
                viewModel.addToCart(rec)
                showCartScreen()
                updateCartBadge()
            }
        )

        cartBinding.rvCartItems.layoutManager = LinearLayoutManager(this)
        cartBinding.rvCartItems.adapter = cartAdapter

        cartBinding.rvRecommended.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cartBinding.rvRecommended.adapter = recAdapter

        cartBinding.btnProceedCheckout.setOnClickListener {
            if (shopState.cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty. Add items to checkout.", Toast.LENGTH_SHORT).show()
            } else {
                showCheckoutDialog()
            }
        }
    }

    private fun showProfileScreen() {
        val profileBinding = FragmentProfileBinding.inflate(layoutInflater)
        binding.fragmentContainer.removeAllViews()
        binding.fragmentContainer.addView(profileBinding.root)

        val profile = viewModel.userProfileFlow.value
        val orders = viewModel.ordersListFlow.value

        profileBinding.apply {
            tvProfileName.text = profile.fullName.ifBlank { "Omni Customer" }
            tvProfileEmail.text = profile.email
            tvProfilePhone.text = profile.phone
            tvProfileAddress.text = profile.address.formattedAddress()

            val orderAdapter = OrderAdapter(orders)
            rvOrders.layoutManager = LinearLayoutManager(this@MainActivity)
            rvOrders.adapter = orderAdapter

            btnSignOut.setOnClickListener {
                viewModel.signOut()
            }
        }
    }

    private fun showCheckoutDialog() {
        val dialog = BottomSheetDialog(this)
        val dialogBinding = DialogCheckoutBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val userProfile = viewModel.userProfileFlow.value

        dialogBinding.apply {
            etCheckoutName.setText(userProfile.fullName)
            etCheckoutPhone.setText(userProfile.phone)
            etCheckoutPincode.setText(userProfile.address.pincode)
            etCheckoutStreet.setText(userProfile.address.street)
            etCheckoutCity.setText(userProfile.address.city)
            etCheckoutState.setText(userProfile.address.state)

            etCheckoutPincode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val pin = s.toString().trim()
                    if (pin.length >= 2) {
                        val loc = PincodeLookup.lookupLocation(pin)
                        if (loc != null) {
                            etCheckoutCity.setText(loc.city)
                            etCheckoutState.setText(loc.state)
                        }
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            btnToPayment.setOnClickListener {
                layoutStepAddress.visibility = View.GONE
                layoutStepPayment.visibility = View.VISIBLE
                tvCheckoutStepTitle.text = "Select Payment Method"
            }

            btnConfirmOrder.setOnClickListener {
                val addr = DeliveryAddress(
                    street = etCheckoutStreet.text.toString(),
                    city = etCheckoutCity.text.toString(),
                    state = etCheckoutState.text.toString(),
                    pincode = etCheckoutPincode.text.toString()
                )
                val payment = when (rgPayment.checkedRadioButtonId) {
                    R.id.rbCard -> "Credit / Debit Card"
                    R.id.rbCod -> "Cash on Delivery (COD)"
                    else -> "UPI (Google Pay / PhonePe)"
                }

                val placedOrder = viewModel.placeOrder(addr, etCheckoutPhone.text.toString(), payment)

                layoutStepPayment.visibility = View.GONE
                layoutStepSuccess.visibility = View.VISIBLE
                tvCheckoutStepTitle.text = "Order Confirmed!"
                tvReceiptOrderId.text = "Order ID: ${placedOrder.orderId}"

                updateCartBadge()
            }

            btnCloseReceipt.setOnClickListener {
                dialog.dismiss()
                binding.bottomNavigation.selectedItemId = R.id.nav_profile
                showProfileScreen()
            }
        }

        dialog.show()
    }

    private fun updateCartBadge() {
        val cartCount = viewModel.shopUiState.value.cartItems.sumOf { it.quantity }
        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.nav_cart)
        if (cartCount > 0) {
            badge.isVisible = true
            badge.number = cartCount
        } else {
            badge.isVisible = false
        }
    }
}
