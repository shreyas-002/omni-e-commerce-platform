package com.example.omnie_commerceplatform.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnie_commerceplatform.data.CartItem
import com.example.omnie_commerceplatform.data.OmniData
import com.example.omnie_commerceplatform.data.Order
import com.example.omnie_commerceplatform.data.Product
import com.example.omnie_commerceplatform.data.UserProfile
import com.example.omnie_commerceplatform.ui.ShopUiState
import com.example.omnie_commerceplatform.ui.components.OmniLogo
import com.example.omnie_commerceplatform.ui.theme.OmniAccentPink
import com.example.omnie_commerceplatform.ui.theme.OmniBorder
import com.example.omnie_commerceplatform.ui.theme.OmniCreamBg
import com.example.omnie_commerceplatform.ui.theme.OmniDarkNavy
import com.example.omnie_commerceplatform.ui.theme.OmniMutedGold
import com.example.omnie_commerceplatform.ui.theme.OmniSuccessGreen
import com.example.omnie_commerceplatform.ui.theme.OmniTextMuted
import com.example.omnie_commerceplatform.ui.theme.OmniTextPrimary
import com.example.omnie_commerceplatform.ui.theme.OmniTextSecondary
import java.util.Locale

@Composable
fun HomeScreen(
    shopState: ShopUiState,
    products: List<Product>,
    userProfile: UserProfile,
    orders: List<Order>,
    onCategorySelected: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onResetFilters: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onQuantityChange: (productId: String, delta: Int) -> Unit,
    onOpenCart: () -> Unit,
    onOpenCheckout: () -> Unit,
    onSignOut: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Home, 1: Explore, 2: Cart, 3: Profile

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(OmniCreamBg)) {
        val screenWidth = this.maxWidth
        val gridColumns = when {
            screenWidth >= 900.dp -> 4
            screenWidth >= 600.dp -> 3
            else -> 2
        }

        Column(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
            // Top Bar Navigation
            TopHeaderBar(
                searchQuery = shopState.searchQuery,
                cartCount = shopState.cartItems.sumOf { it.quantity },
                onSearchQueryChanged = onSearchQueryChanged,
                onOpenCart = {
                    selectedTab = 2
                }
            )

            // Main View Area
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0, 1 -> {
                        // Home & Explore Tab Grid
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(gridColumns),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            if (selectedTab == 0) {
                                item(span = { GridItemSpan(gridColumns) }) {
                                    HeroBannerSection(onExploreClick = { selectedTab = 1 })
                                }
                            }

                            // Category Filter Pills
                            item(span = { GridItemSpan(gridColumns) }) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = if (selectedTab == 0) "EXPLORE THE COLLECTION" else "ALL CATEGORIES",
                                        color = OmniAccentPink,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.5.sp
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = shopState.selectedCategory,
                                            color = OmniDarkNavy,
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif
                                        )

                                        Text(
                                            text = "${products.size} ${if (products.size == 1) "item" else "items"}",
                                            color = OmniTextSecondary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        contentPadding = PaddingValues(end = 16.dp)
                                    ) {
                                        items(OmniData.categories) { category ->
                                            val isSelected = category == shopState.selectedCategory
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(if (isSelected) OmniDarkNavy else Color.White)
                                                    .border(
                                                        1.dp,
                                                        if (isSelected) OmniDarkNavy else OmniBorder,
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .clickable { onCategorySelected(category) }
                                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                            ) {
                                                Text(
                                                    text = category,
                                                    color = if (isSelected) Color.White else OmniTextPrimary,
                                                    fontSize = 13.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            if (products.isEmpty()) {
                                item(span = { GridItemSpan(gridColumns) }) {
                                    EmptySearchState(
                                        searchQuery = shopState.searchQuery,
                                        category = shopState.selectedCategory,
                                        onResetFilters = onResetFilters
                                    )
                                }
                            } else {
                                items(products, key = { it.id }) { product ->
                                    Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                                        ProductCard(
                                            product = product,
                                            onAddToCart = onAddToCart
                                        )
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Direct Full Amazon/Myntra Style Cart Screen
                        FullCartPage(
                            cartItems = shopState.cartItems,
                            allProducts = OmniData.products,
                            onQuantityChange = onQuantityChange,
                            onAddToCart = onAddToCart,
                            onOpenCheckout = onOpenCheckout,
                            onExploreStore = { selectedTab = 0 }
                        )
                    }

                    3 -> {
                        // Profile & Account Tab
                        ProfileAccountScreen(
                            userProfile = userProfile,
                            orders = orders,
                            cartCount = shopState.cartItems.sumOf { it.quantity },
                            onSignOut = onSignOut
                        )
                    }
                }
            }

            // Material 3 Bottom Navigation Bar
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OmniAccentPink,
                        selectedTextColor = OmniAccentPink,
                        indicatorColor = OmniCreamBg
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.GridView, contentDescription = "Explore") },
                    label = { Text("Explore", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OmniAccentPink,
                        selectedTextColor = OmniAccentPink,
                        indicatorColor = OmniCreamBg
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        BadgedBox(
                            badge = {
                                val cartCount = shopState.cartItems.sumOf { it.quantity }
                                if (cartCount > 0) {
                                    Badge(containerColor = OmniAccentPink, contentColor = Color.White) {
                                        Text(cartCount.toString(), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OmniAccentPink,
                        selectedTextColor = OmniAccentPink,
                        indicatorColor = OmniCreamBg
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OmniAccentPink,
                        selectedTextColor = OmniAccentPink,
                        indicatorColor = OmniCreamBg
                    )
                )
            }
        }
    }
}

/**
 * Direct Full Amazon/Myntra Style Cart Page
 */
@Composable
fun FullCartPage(
    cartItems: List<CartItem>,
    allProducts: List<Product>,
    onQuantityChange: (productId: String, delta: Int) -> Unit,
    onAddToCart: (Product) -> Unit,
    onOpenCheckout: () -> Unit,
    onExploreStore: () -> Unit
) {
    val totalPrice = cartItems.sumOf { it.product.priceInRupees * it.quantity }
    val cartProductIds = remember(cartItems) { cartItems.map { it.product.id }.toSet() }
    val recommendedProducts = remember(cartProductIds) {
        allProducts.filter { it.id !in cartProductIds }
    }

    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Empty",
                    modifier = Modifier.size(72.dp),
                    tint = OmniTextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Your Cart is Empty", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Explore thousands of products and add your favorites to cart.", fontSize = 13.sp, color = OmniTextSecondary, textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onExploreStore,
                    colors = ButtonDefaults.buttonColors(containerColor = OmniDarkNavy),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Explore Products →", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("MY SHOPPING CART", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OmniAccentPink, letterSpacing = 1.5.sp)
                            Text("${cartItems.sumOf { it.quantity }} items selected", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, fontFamily = FontFamily.Serif)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEAFAF1))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("⚡ Free Shipping Active", color = Color(0xFF155724), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Item Cards List
                items(cartItems, key = { it.product.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, OmniBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Product Image
                            Box(
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(item.product.accentColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = item.product.imageUrl.ifBlank { item.product.drawableRes },
                                    contentDescription = item.product.title,
                                    contentScale = ContentScale.Crop,
                                    error = painterResource(id = item.product.drawableRes),
                                    placeholder = painterResource(id = item.product.drawableRes),
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.product.brandTagline, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OmniTextMuted, letterSpacing = 1.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(item.product.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = OmniDarkNavy, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("₹${String.format(Locale.US, "%,d", item.product.priceInRupees)}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = OmniDarkNavy)
                                Text("Delivery by 3 business days", fontSize = 11.sp, color = OmniSuccessGreen, fontWeight = FontWeight.SemiBold)
                            }

                            // Quantity Modifier Controls
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .border(1.dp, OmniBorder, CircleShape)
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                IconButton(onClick = { onQuantityChange(item.product.id, -1) }, modifier = Modifier.size(28.dp)) {
                                    Icon(if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Remove, contentDescription = "Decrease", tint = if (item.quantity == 1) OmniAccentPink else OmniDarkNavy, modifier = Modifier.size(16.dp))
                                }

                                Text(item.quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 6.dp))

                                IconButton(onClick = { onQuantityChange(item.product.id, 1) }, modifier = Modifier.size(28.dp)) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = OmniDarkNavy, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                // "Customers Also Added" Add More Items Carousel
                if (recommendedProducts.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            Text("CUSTOMERS ALSO ADDED TO CART", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(recommendedProducts.take(6), key = { it.id }) { rec ->
                                    Card(
                                        modifier = Modifier.width(160.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, OmniBorder)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(84.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(rec.accentColorHex)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                AsyncImage(
                                                    model = rec.imageUrl.ifBlank { rec.drawableRes },
                                                    contentDescription = rec.title,
                                                    contentScale = ContentScale.Crop,
                                                    error = painterResource(id = rec.drawableRes),
                                                    placeholder = painterResource(id = rec.drawableRes),
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(rec.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                            Text("₹${String.format(Locale.US, "%,d", rec.priceInRupees)}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = OmniDarkNavy)

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Button(
                                                onClick = { onAddToCart(rec) },
                                                modifier = Modifier.fillMaxWidth().height(32.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = OmniCreamBg),
                                                shape = RoundedCornerShape(6.dp),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Text("+ Add to Cart", color = OmniDarkNavy, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Itemized Price Details Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, OmniBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("PRICE DETAILS (${cartItems.sumOf { it.quantity }} items)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total MRP", color = OmniTextSecondary, fontSize = 13.sp)
                                Text("₹${String.format(Locale.US, "%,d", totalPrice)}", color = OmniTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Express Delivery Fee", color = OmniTextSecondary, fontSize = 13.sp)
                                Text("FREE", color = OmniSuccessGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = OmniBorder)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Amount Payable", fontWeight = FontWeight.Bold, color = OmniDarkNavy, fontSize = 15.sp)
                                Text("₹${String.format(Locale.US, "%,d", totalPrice)}", fontWeight = FontWeight.Bold, color = OmniDarkNavy, fontSize = 17.sp, fontFamily = FontFamily.Serif)
                            }
                        }
                    }
                }
            }

            // Sticky Bottom Checkout Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TOTAL PAYABLE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OmniTextMuted, letterSpacing = 1.sp)
                        Text("₹${String.format(Locale.US, "%,d", totalPrice)}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = OmniDarkNavy, fontFamily = FontFamily.Serif)
                    }

                    Button(
                        onClick = onOpenCheckout,
                        modifier = Modifier.height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OmniDarkNavy),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        Text("Proceed to Checkout →", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TopHeaderBar(
    searchQuery: String,
    cartCount: Int,
    onSearchQueryChanged: (String) -> Unit,
    onOpenCart: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OmniLogo(textColor = OmniDarkNavy, iconSize = 30.dp, textSizeSp = 20, showTagline = false)

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(OmniCreamBg)
                .border(1.dp, OmniBorder, RoundedCornerShape(22.dp))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = OmniTextSecondary,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search products...",
                            fontSize = 13.sp,
                            color = OmniTextMuted,
                            maxLines = 1
                        )
                    }

                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChanged,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = OmniTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        cursorBrush = SolidColor(OmniDarkNavy),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchQueryChanged("") },
                        modifier = Modifier.size(22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = OmniTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        IconButton(onClick = onOpenCart) {
            BadgedBox(
                badge = {
                    if (cartCount > 0) {
                        Badge(containerColor = OmniAccentPink, contentColor = Color.White) {
                            Text(cartCount.toString(), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Cart",
                    tint = OmniDarkNavy,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun HeroBannerSection(
    onExploreClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(OmniDarkNavy, Color(0xFF0F1B38))
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Column {
            Text(
                text = "THE OMNI EDIT",
                color = OmniAccentPink,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Elevate Your\nEveryday.",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Discover curated tech, apparel, and lifestyle essentials crafted for modern living.",
                color = OmniMutedGold,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onExploreClick,
                colors = ButtonDefaults.buttonColors(containerColor = OmniAccentPink),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Explore Collection →", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, OmniBorder, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.1f)
                    .background(Color(product.accentColorHex)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.imageUrl.ifBlank { product.drawableRes },
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = product.drawableRes),
                    placeholder = painterResource(id = product.drawableRes),
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(OmniDarkNavy.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = product.category,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = product.brandTagline,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = OmniTextMuted,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = OmniTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${String.format(Locale.US, "%,d", product.priceInRupees)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OmniDarkNavy
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = OmniMutedGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${product.rating} (${product.reviewCount})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OmniTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OmniCreamBg),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, OmniBorder)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = OmniDarkNavy, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add to Cart", color = OmniDarkNavy, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileAccountScreen(
    userProfile: UserProfile,
    orders: List<Order>,
    cartCount: Int,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("MY ACCOUNT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OmniAccentPink, letterSpacing = 1.5.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Profile & Orders", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, fontFamily = FontFamily.Serif)

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OmniBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(OmniDarkNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(userProfile.fullName.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(userProfile.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OmniDarkNavy)
                        Text(userProfile.email, fontSize = 12.sp, color = OmniTextSecondary)
                        Text(userProfile.phone, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OmniAccentPink)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = OmniBorder)

                Text("DEFAULT DELIVERY LOCATION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(userProfile.address.formattedAddress(), fontSize = 12.sp, color = OmniTextSecondary)

                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = OmniBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Cart Items", color = OmniTextSecondary, fontSize = 13.sp)
                    Text("$cartCount items", fontWeight = FontWeight.Bold, color = OmniDarkNavy, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("ORDER HISTORY & TRACKING (${orders.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(10.dp))

        orders.forEach { order ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, OmniBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(order.orderId, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OmniDarkNavy)
                            Text("Placed: ${order.orderDate}", fontSize = 11.sp, color = OmniTextMuted)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEAFAF1))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(order.orderStatus, color = Color(0xFF155724), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.quantity}x ${item.product.title}", fontSize = 12.sp, color = OmniTextPrimary, maxLines = 1, modifier = Modifier.weight(1f))
                            Text("₹${String.format(Locale.US, "%,d", item.product.priceInRupees * item.quantity)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = OmniBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Paid (${order.paymentMethod})", fontSize = 12.sp, color = OmniTextSecondary)
                        Text("₹${String.format(Locale.US, "%,d", order.totalAmount)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, fontFamily = FontFamily.Serif)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Est. Delivery: ${order.estimatedDelivery}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OmniSuccessGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSignOut,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDEDEC)),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF5C6CB))
        ) {
            Text("Sign Out of Omni", color = Color(0xFF721C24), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun EmptySearchState(
    searchQuery: String,
    category: String,
    onResetFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, OmniBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = "No results",
                tint = OmniTextMuted,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No products found",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = OmniDarkNavy
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (searchQuery.isNotBlank())
                "We couldn't find anything matching \"$searchQuery\" in $category."
            else
                "There are currently no products available in $category.",
            fontSize = 14.sp,
            color = OmniTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onResetFilters,
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, OmniDarkNavy)
        ) {
            Text("Reset Search & Filters", color = OmniDarkNavy, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}
