package com.example.omnie_commerceplatform.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnie_commerceplatform.data.CartItem
import com.example.omnie_commerceplatform.ui.theme.OmniAccentPink
import com.example.omnie_commerceplatform.ui.theme.OmniBorder
import com.example.omnie_commerceplatform.ui.theme.OmniCreamBg
import com.example.omnie_commerceplatform.ui.theme.OmniDarkNavy
import com.example.omnie_commerceplatform.ui.theme.OmniSuccessGreen
import com.example.omnie_commerceplatform.ui.theme.OmniTextPrimary
import com.example.omnie_commerceplatform.ui.theme.OmniTextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    sheetState: SheetState,
    cartItems: List<CartItem>,
    onDismiss: () -> Unit,
    onQuantityChange: (productId: String, delta: Int) -> Unit,
    onClearCart: () -> Unit,
    onCheckout: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        val totalPrice = cartItems.sumOf { it.product.priceInRupees * it.quantity }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Cart",
                        tint = OmniDarkNavy,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Your Cart (${cartItems.sumOf { it.quantity }})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OmniDarkNavy
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (cartItems.isNotEmpty()) {
                        IconButton(onClick = onClearCart) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear",
                                tint = OmniAccentPink
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = OmniTextSecondary
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = OmniBorder)

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Empty",
                            modifier = Modifier.size(56.dp),
                            tint = OmniTextSecondary.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Your cart is currently empty",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = OmniTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cartItems, key = { it.product.id }) { item ->
                        CartItemRow(
                            item = item,
                            onQuantityChange = onQuantityChange
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = OmniBorder)

                // Subtotal & Checkout Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OmniCreamBg, shape = RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", color = OmniTextSecondary, fontSize = 14.sp)
                        Text("₹${String.format(Locale.US, "%,d", totalPrice)}", color = OmniTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Express Shipping", color = OmniTextSecondary, fontSize = 14.sp)
                        Text("FREE", color = OmniSuccessGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = OmniBorder)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount", color = OmniDarkNavy, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = "₹${String.format(Locale.US, "%,d", totalPrice)}",
                            color = OmniDarkNavy,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onCheckout()
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OmniDarkNavy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Proceed to Checkout →", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onQuantityChange: (productId: String, delta: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, OmniBorder, RoundedCornerShape(10.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Thumbnail Image
        Box(
            modifier = Modifier
                .size(56.dp)
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

        Spacer(modifier = Modifier.width(12.dp))

        // Title and Price
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = OmniTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "₹${String.format(Locale.US, "%,d", item.product.priceInRupees)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = OmniDarkNavy
            )
        }

        // Quantity Controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(1.dp, OmniBorder, CircleShape)
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            IconButton(
                onClick = { onQuantityChange(item.product.id, -1) },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = OmniDarkNavy, modifier = Modifier.size(16.dp))
            }

            Text(
                text = item.quantity.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            IconButton(
                onClick = { onQuantityChange(item.product.id, 1) },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase", tint = OmniDarkNavy, modifier = Modifier.size(16.dp))
            }
        }
    }
}
