package com.example.omnie_commerceplatform.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnie_commerceplatform.data.DemoAccount
import com.example.omnie_commerceplatform.data.OmniData
import com.example.omnie_commerceplatform.ui.AuthUiState
import com.example.omnie_commerceplatform.ui.components.OmniLogo
import com.example.omnie_commerceplatform.ui.theme.OmniAccentPink
import com.example.omnie_commerceplatform.ui.theme.OmniBorder
import com.example.omnie_commerceplatform.ui.theme.OmniDarkNavy
import com.example.omnie_commerceplatform.ui.theme.OmniSuccessGreen
import com.example.omnie_commerceplatform.ui.theme.OmniTextMuted
import com.example.omnie_commerceplatform.ui.theme.OmniTextPrimary
import com.example.omnie_commerceplatform.ui.theme.OmniTextSecondary

@Composable
fun SignInScreen(
    uiState: AuthUiState,
    onToggleAuthMode: () -> Unit,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onStreetChange: (String) -> Unit,
    onPincodeChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onStateChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleRememberMe: () -> Unit,
    onSelectDemoAccount: (DemoAccount) -> Unit,
    onSignIn: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        OmniDarkNavy,
                        Color(0xFF070D1D),
                        Color(0xFF101C38)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Logo
            OmniLogo(
                textColor = Color.White,
                taglineColor = OmniAccentPink,
                iconSize = 44.dp,
                textSizeSp = 30
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Auth Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isSignUpMode) "REGISTER NEW ACCOUNT" else "WELCOME BACK",
                            color = OmniAccentPink,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )

                        TextButton(
                            onClick = onToggleAuthMode,
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (uiState.isSignUpMode) "Sign In instead" else "Create Account",
                                fontSize = 12.sp,
                                color = OmniDarkNavy,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (uiState.isSignUpMode) "Join Omni Store" else "Sign in to Omni",
                        color = OmniDarkNavy,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (uiState.isSignUpMode) "Set up your profile, address & phone number." else "Access your saved items, cart, and account.",
                        color = OmniTextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (uiState.isSignUpMode) {
                        // Full Name Input
                        Text("Full Name", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OmniTextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = uiState.fullName,
                            onValueChange = onFullNameChange,
                            placeholder = { Text("e.g. John Doe", color = OmniTextMuted, fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = OmniTextSecondary, modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OmniDarkNavy, unfocusedBorderColor = OmniBorder)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Email Field
                    Text("Email address", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OmniTextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = onEmailChange,
                        placeholder = { Text("name@example.com", color = OmniTextMuted, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = OmniTextSecondary, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OmniDarkNavy, unfocusedBorderColor = OmniBorder)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    Text("Password", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OmniTextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.pass,
                        onValueChange = onPasswordChange,
                        placeholder = { Text("••••••••", color = OmniTextMuted, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = OmniTextSecondary, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = onTogglePasswordVisibility) {
                                Icon(
                                    imageVector = if (uiState.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Password",
                                    tint = OmniTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OmniDarkNavy, unfocusedBorderColor = OmniBorder)
                    )

                    if (uiState.isSignUpMode) {
                        Spacer(modifier = Modifier.height(12.dp))

                        // Phone Number Field
                        Text("Phone Number (+91)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OmniTextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = uiState.phone,
                            onValueChange = onPhoneChange,
                            placeholder = { Text("+91 98765 43210", color = OmniTextMuted, fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = OmniTextSecondary, modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OmniDarkNavy, unfocusedBorderColor = OmniBorder)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Delivery Street Address
                        Text("Delivery Street Location", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OmniTextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = uiState.streetAddress,
                            onValueChange = onStreetChange,
                            placeholder = { Text("Building, Flat No., Street Name", color = OmniTextMuted, fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = OmniTextSecondary, modifier = Modifier.size(18.dp)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OmniDarkNavy, unfocusedBorderColor = OmniBorder)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // PIN Code Field
                        Text("PIN Code", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OmniTextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = uiState.pincode,
                            onValueChange = onPincodeChange,
                            placeholder = { Text("e.g. 560001", color = OmniTextMuted, fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OmniDarkNavy, unfocusedBorderColor = OmniBorder)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("City", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OmniTextPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = uiState.city,
                                    onValueChange = onCityChange,
                                    placeholder = { Text("City", color = OmniTextMuted, fontSize = 13.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OmniDarkNavy, unfocusedBorderColor = OmniBorder)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("State", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OmniTextPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = uiState.state,
                                    onValueChange = onStateChange,
                                    placeholder = { Text("State", color = OmniTextMuted, fontSize = 13.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OmniDarkNavy, unfocusedBorderColor = OmniBorder)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Error Message Banner
                    AnimatedVisibility(
                        visible = uiState.errorMessage != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        uiState.errorMessage?.let { msg ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFDEDEC))
                                    .border(1.dp, Color(0xFFF5C6CB), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Error, contentDescription = "Error", tint = OmniAccentPink, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(msg, color = Color(0xFF721C24), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Remember Me Option
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onToggleRememberMe() }
                    ) {
                        Checkbox(
                            checked = uiState.rememberMe,
                            onCheckedChange = { onToggleRememberMe() },
                            colors = CheckboxDefaults.colors(checkedColor = OmniDarkNavy)
                        )
                        Text("Remember me on this device", fontSize = 12.sp, color = OmniTextSecondary)
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Sign In / Register Button
                    Button(
                        onClick = onSignIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OmniDarkNavy),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (uiState.isSignUpMode) "Register & Open Store" else "Sign in", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }

                    // Success Banner
                    AnimatedVisibility(
                        visible = uiState.successMessage != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        uiState.successMessage?.let { msg ->
                            Row(
                                modifier = Modifier
                                    .padding(top = 14.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEAFAF1))
                                    .border(1.dp, Color(0xFFC3E6CB), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = OmniSuccessGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(msg, color = Color(0xFF155724), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = OmniBorder)
                        Text(
                            " or continue with ",
                            color = OmniTextMuted,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = OmniBorder)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Google Sign In
                    OutlinedButton(
                        onClick = onSignIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, OmniBorder)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(OmniDarkNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("G", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Continue with Google", color = OmniTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Quick Demo Accounts Box
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF8F9FA))
                            .border(1.dp, OmniBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("QUICK DEMO ACCESS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy, letterSpacing = 1.sp)
                            Text("3 ACCOUNTS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OmniAccentPink, letterSpacing = 1.sp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OmniData.demoAccounts.forEach { demo ->
                            val isSelected = uiState.email == demo.email
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) OmniDarkNavy.copy(alpha = 0.08f) else Color.Transparent)
                                    .clickable { onSelectDemoAccount(demo) }
                                    .padding(vertical = 6.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(demo.label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OmniDarkNavy)
                                    Text(demo.email, fontSize = 11.sp, color = OmniTextSecondary)
                                }
                                Text(
                                    text = if (isSelected) "Selected ✓" else "Select",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) OmniAccentPink else OmniTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
