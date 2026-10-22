package com.example.omnie_commerceplatform.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnie_commerceplatform.R
import com.example.omnie_commerceplatform.ui.theme.OmniAccentPink
import com.example.omnie_commerceplatform.ui.theme.OmniDarkNavy

/**
 * Premium brand logo for Omni E-Commerce.
 * Renders the signature 3D sweeping gradient 'O' shopping cart vector mark.
 */
@Composable
fun OmniLogoIcon(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    Image(
        painter = painterResource(id = R.drawable.ic_omni_logo),
        contentDescription = "Omni Logo",
        modifier = modifier.size(size)
    )
}

@Composable
fun OmniLogo(
    modifier: Modifier = Modifier,
    textColor: Color = OmniDarkNavy,
    taglineColor: Color = OmniAccentPink,
    iconSize: Dp = 32.dp,
    textSizeSp: Int = 22,
    showTagline: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OmniLogoIcon(size = iconSize)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "omni",
                color = textColor,
                fontSize = textSizeSp.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                letterSpacing = (-0.5).sp
            )
            if (showTagline) {
                Text(
                    text = "E-COMMERCE",
                    color = taglineColor,
                    fontSize = (textSizeSp * 0.36f).sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}
