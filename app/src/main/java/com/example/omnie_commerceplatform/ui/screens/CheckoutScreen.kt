package com.example.omnie_commerceplatform.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnie_commerceplatform.data.CartItem
import com.example.omnie_commerceplatform.data.DeliveryAddress
import com.example.omnie_commerceplatform.data.Order
import com.example.omnie_commerceplatform.data.PincodeLookup
import com.example.omnie_commerceplatform.ui.theme.OmniAccentPink
import com.example.omnie_commerceplatform.ui.theme.OmniBorder
import com.example.omnie_commerceplatform.ui.theme.OmniCreamBg
import com.example.omnie_commerceplatform.ui.theme.OmniDarkNavy
import com.example.omnie_commerceplatform.ui.theme.OmniSuccessGreen
import com.example.omnie_commerceplatform.ui.theme.OmniTextMuted
import com.example.omnie_commerceplatform.ui.theme.OmniTextPrimary
import com.example.omnie_commerceplatform.ui.theme.OmniTextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutModalSheet(
    sheetState: SheetState,
    cartItems: List<CartItem>,
    userPhone: String,
    userAddress: DeliveryAddress,
    onDismiss: () -> Unit,
    onPlaceOrder: (address: DeliveryAddress, phone: String, paymentMethod: String) -> Order
) {
    var step by remember { mutableIntStateOf(1) } // 1: Address, 2: Payment, 3: Success

    var nameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf(userPhone) }
    var streetInput by remember { mutableStateOf(userAddress.street) }
    var cityInput by remember { mutableStateOf(userAddress.city) }
    var stateInput by remember { mutableStateOf(userAddress.state) }
    var pincodeInput by remember { mutableStateOf(userAddress.pincode) }
    var paymentMethod by remember { mutableStateOf("UPI (Google Pay / PhonePe)") }
    var createdOrder by remember { mutableStateOf<Order?>(null) }

    val totalPrice = cartItems.sumOf { it.product.priceInRupees * it.quantity }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (step) {
                        1 -> "Shipping & Delivery"
                        2 -> "Payment Method"
                        else -> "Order Confirmed!"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = OmniDarkNavy,
                    fontFamily = FontFamily.Serif
                )

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = OmniTextSecondary)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = OmniBorder)

            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "CheckoutStepTransition"
            ) { currentStep ->
                when (currentStep) {
                    1 -> {
                        // Step 1: Address & Contact Form
                        Column {
                            Text("ENTER DELIVERY ADDRESS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OmniAccentPink, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = nameInput,
                                onValueChange = { nameInput = it },
                                label = { Text("Full Name") },
                                placeholder = { Text("e.g. John Doe", color = OmniTextMuted) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = phoneInput,
                                onValueChange = { phoneInput = it },
                                label = { Text("Phone Number") },
                                placeholder = { Text("+91 98765 43210", color = OmniTextMuted) },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = pincodeInput,
                                onValueChange = { newPin ->
                                    pincodeInput = newPin
                                    val loc = PincodeLookup.lookupLocation(newPin)
                                    if (loc != null) {
                                        cityInput = loc.city
                                        stateInput = loc.state
                                    }
                                },
                                label = { Text("PIN Code") },
                                placeholder = { Text("e.g. 560001", color = OmniTextMuted) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = streetInput,
                                onValueChange = { streetInput = it },
                                label = { Text("Street Address / Flat No. / Area") },
                                placeholder = { Text("House No., Road, Area", color = OmniTextMuted) },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = cityInput,
                                    onValueChange = { newCity ->
                                        cityInput = newCity
                                        val suggested = PincodeLookup.lookupPincode(newCity)
                                        if (suggested != null && pincodeInput.isBlank()) {
                                            pincodeInput = suggested
                                        }
                                    },
                                    label = { Text("City") },
                                    placeholder = { Text("City", color = OmniTextMuted) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = stateInput,
                                    onValueChange = { stateInput = it },
                                    label = { Text("State") },
                                    placeholder = { Text("State", color = OmniTextMuted) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { step = 2 },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OmniDarkNavy),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Continue to Payment →", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    2 -> {
                        // Step 2: Payment Selection
                        Column {
                            Text("SELECT PAYMENT METHOD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OmniAccentPink, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(12.dp))

                            val paymentOptions = listOf(
                                "UPI (Google Pay / PhonePe / PayTM)",
                                "Credit / Debit Card",
                                "Cash on Delivery (COD)",
                                "Net Banking"
                            )

                            paymentOptions.forEach { option ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { paymentMethod = option },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (paymentMethod == option) OmniCreamBg else Color.White),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (paymentMethod == option) OmniDarkNavy else OmniBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = paymentMethod == option,
                                            onClick = { paymentMethod = option },
                                            colors = RadioButtonDefaults.colors(selectedColor = OmniDarkNavy)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = if (option.contains("UPI")) Icons.Default.Payment else Icons.Default.CreditCard,
                                            contentDescription = null,
                                            tint = OmniDarkNavy
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(option, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = OmniDarkNavy)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Price Summary Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = OmniCreamBg)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Subtotal (${cartItems.sumOf { it.quantity }} items)", color = OmniTextSecondary, fontSize = 13.sp)
                                        Text("₹${String.format(Locale.US, "%,d", totalPrice)}", fontWeight = FontWeight.Bold, color = OmniTextPrimary, fontSize = 13.sp)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Express Shipping", color = OmniTextSecondary, fontSize = 13.sp)
                                        Text("FREE", fontWeight = FontWeight.Bold, color = OmniSuccessGreen, fontSize = 13.sp)
                                    }
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = OmniBorder)
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Grand Total", fontWeight = FontWeight.Bold, color = OmniDarkNavy, fontSize = 15.sp)
                                        Text("₹${String.format(Locale.US, "%,d", totalPrice)}", fontWeight = FontWeight.Bold, color = OmniDarkNavy, fontSize = 16.sp, fontFamily = FontFamily.Serif)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    val addr = DeliveryAddress(streetInput, cityInput, stateInput, pincodeInput)
                                    createdOrder = onPlaceOrder(addr, phoneInput, paymentMethod)
                                    step = 3
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OmniDarkNavy),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Confirm & Place Order (₹${String.format(Locale.US, "%,d", totalPrice)})", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    else -> {
                        // Step 3: Success Confirmation Receipt
                        createdOrder?.let { order ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEAFAF1)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = OmniSuccessGreen, modifier = Modifier.size(40.dp))
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("Order Placed Successfully!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, fontFamily = FontFamily.Serif)
                                Text("Order ID: ${order.orderId}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OmniAccentPink)

                                Spacer(modifier = Modifier.height(16.dp))

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = OmniCreamBg)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("DELIVERY RECEIPT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, letterSpacing = 1.sp)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Contact Phone: ${order.contactPhone}", fontSize = 12.sp, color = OmniTextPrimary, fontWeight = FontWeight.SemiBold)
                                        Text("Address: ${order.deliveryAddress.formattedAddress()}", fontSize = 12.sp, color = OmniTextSecondary)
                                        Text("Payment: ${order.paymentMethod}", fontSize = 12.sp, color = OmniTextSecondary)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("Est. Delivery: ${order.estimatedDelivery}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OmniSuccessGreen)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = onDismiss,
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = OmniDarkNavy),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Back to Store", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
