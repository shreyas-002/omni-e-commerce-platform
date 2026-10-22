package com.example.omnie_commerceplatform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.omnie_commerceplatform.ui.OmniViewModel
import com.example.omnie_commerceplatform.ui.components.CartBottomSheet
import com.example.omnie_commerceplatform.ui.screens.CheckoutModalSheet
import com.example.omnie_commerceplatform.ui.screens.HomeScreen
import com.example.omnie_commerceplatform.ui.screens.SignInScreen
import com.example.omnie_commerceplatform.ui.screens.SplashScreen
import com.example.omnie_commerceplatform.ui.theme.OmniTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val viewModel: OmniViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OmniTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var showSplash by remember { mutableStateOf(true) }

                    if (showSplash) {
                        SplashScreen(
                            onSplashFinished = { showSplash = false }
                        )
                    } else {
                        val authState by viewModel.authUiState.collectAsState()
                        val shopState by viewModel.shopUiState.collectAsState()
                        val products by viewModel.filteredProductsFlow.collectAsState()
                        val userProfile by viewModel.userProfileFlow.collectAsState()
                        val ordersList by viewModel.ordersListFlow.collectAsState()

                        val cartSheetState = rememberModalBottomSheetState()
                        val checkoutSheetState = rememberModalBottomSheetState()

                        AnimatedContent(
                            targetState = authState.isLoggedIn,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "MainScreenTransition"
                        ) { loggedIn ->
                            if (!loggedIn) {
                                SignInScreen(
                                    uiState = authState,
                                    onToggleAuthMode = viewModel::toggleAuthMode,
                                    onFullNameChange = viewModel::onFullNameChange,
                                    onEmailChange = viewModel::onEmailChange,
                                    onPasswordChange = viewModel::onPasswordChange,
                                    onPhoneChange = viewModel::onPhoneChange,
                                    onStreetChange = viewModel::onStreetChange,
                                    onPincodeChange = viewModel::onPincodeChange,
                                    onCityChange = viewModel::onCityChange,
                                    onStateChange = viewModel::onStateChange,
                                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                                    onToggleRememberMe = viewModel::toggleRememberMe,
                                    onSelectDemoAccount = viewModel::selectDemoAccount,
                                    onSignIn = viewModel::signIn
                                )
                            } else {
                                HomeScreen(
                                    shopState = shopState,
                                    products = products,
                                    userProfile = userProfile,
                                    orders = ordersList,
                                    onCategorySelected = viewModel::onCategorySelected,
                                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                                    onResetFilters = viewModel::resetFilters,
                                    onAddToCart = viewModel::addToCart,
                                    onQuantityChange = viewModel::updateCartQuantity,
                                    onOpenCart = { viewModel.toggleCartOpen(isOpen = true) },
                                    onOpenCheckout = { viewModel.toggleCheckoutOpen(isOpen = true) },
                                    onSignOut = viewModel::signOut
                                )
                            }
                        }

                        if (shopState.isCartOpen) {
                            CartBottomSheet(
                                sheetState = cartSheetState,
                                cartItems = shopState.cartItems,
                                onDismiss = { viewModel.toggleCartOpen(false) },
                                onQuantityChange = viewModel::updateCartQuantity,
                                onClearCart = viewModel::clearCart,
                                onCheckout = {
                                    viewModel.toggleCartOpen(false)
                                    viewModel.toggleCheckoutOpen(true)
                                }
                            )
                        }

                        if (shopState.isCheckoutOpen) {
                            CheckoutModalSheet(
                                sheetState = checkoutSheetState,
                                cartItems = shopState.cartItems,
                                userPhone = userProfile.phone,
                                userAddress = userProfile.address,
                                onDismiss = { viewModel.toggleCheckoutOpen(false) },
                                onPlaceOrder = { address, phone, paymentMethod ->
                                    viewModel.placeOrder(address, phone, paymentMethod)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
