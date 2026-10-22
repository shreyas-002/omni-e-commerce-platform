package com.example.omnie_commerceplatform.data

import com.example.omnie_commerceplatform.R

data class Product(
    val id: String,
    val title: String,
    val category: String,
    val brandTagline: String,
    val priceInRupees: Int,
    val rating: Double,
    val reviewCount: Int = 120,
    val imageUrl: String = "",
    val drawableRes: Int = R.drawable.ic_prod_headphones,
    val accentColorHex: Long = 0xFF1B3B50
)

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)

data class DemoAccount(
    val email: String,
    val pass: String,
    val label: String
)

object OmniData {
    val demoAccounts = listOf(
        DemoAccount("demo@omni.com", "OmniDemo123", "Demo Account"),
        DemoAccount("shopper@omni.com", "Shopper123", "Shopper Account"),
        DemoAccount("test@omni.com", "TestOmni123", "Test Account")
    )

    val categories = listOf(
        "All products",
        "Electronics",
        "Clothing",
        "Footwear",
        "Accessories",
        "Home & Kitchen",
        "Beauty",
        "Sports & Fitness",
        "Books",
        "Gaming",
        "Bags"
    )

    val products = listOf(
        Product(
            id = "p1",
            title = "Wireless Headphones",
            category = "Electronics",
            brandTagline = "TECH ESSENTIALS",
            priceInRupees = 1712,
            rating = 4.9,
            reviewCount = 340,
            imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80",
            drawableRes = R.drawable.ic_prod_headphones,
            accentColorHex = 0xFFD4A373
        ),
        Product(
            id = "p2",
            title = "Bluetooth Speaker",
            category = "Electronics",
            brandTagline = "TECH ESSENTIALS",
            priceInRupees = 2029,
            rating = 4.4,
            reviewCount = 182,
            imageUrl = "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=800&q=80",
            drawableRes = R.drawable.ic_prod_speaker,
            accentColorHex = 0xFF1B3B50
        ),
        Product(
            id = "p3",
            title = "Smart Watch",
            category = "Electronics",
            brandTagline = "TECH ESSENTIALS",
            priceInRupees = 2346,
            rating = 4.5,
            reviewCount = 210,
            imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&q=80",
            drawableRes = R.drawable.ic_prod_watch,
            accentColorHex = 0xFF708090
        ),
        Product(
            id = "p4",
            title = "Wireless Earbuds",
            category = "Electronics",
            brandTagline = "TECH ESSENTIALS",
            priceInRupees = 2663,
            rating = 4.6,
            reviewCount = 420,
            imageUrl = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=800&q=80",
            drawableRes = R.drawable.ic_prod_earbuds,
            accentColorHex = 0xFFE63946
        ),
        Product(
            id = "p5",
            title = "Classic Leather Denim Jacket",
            category = "Clothing",
            brandTagline = "URBAN STYLE",
            priceInRupees = 3499,
            rating = 4.8,
            reviewCount = 95,
            imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800&q=80",
            drawableRes = R.drawable.ic_prod_jacket,
            accentColorHex = 0xFF2A385E
        ),
        Product(
            id = "p6",
            title = "Pro Running Sneakers",
            category = "Footwear",
            brandTagline = "ATHLETIC GEAR",
            priceInRupees = 4299,
            rating = 4.7,
            reviewCount = 160,
            imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800&q=80",
            drawableRes = R.drawable.ic_prod_sneakers,
            accentColorHex = 0xFFE01E5A
        ),
        Product(
            id = "p7",
            title = "Minimalist Stainless Steel Watch",
            category = "Accessories",
            brandTagline = "LUXURY ESSENTIALS",
            priceInRupees = 5199,
            rating = 4.9,
            reviewCount = 280,
            imageUrl = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800&q=80",
            drawableRes = R.drawable.ic_prod_luxury_watch,
            accentColorHex = 0xFF4A4E69
        ),
        Product(
            id = "p8",
            title = "Smart Espresso Coffee Maker",
            category = "Home & Kitchen",
            brandTagline = "KITCHEN PRO",
            priceInRupees = 7999,
            rating = 4.8,
            reviewCount = 115,
            imageUrl = "https://images.unsplash.com/photo-1517668808822-9ed02810a01d?w=800&q=80",
            drawableRes = R.drawable.ic_prod_coffee,
            accentColorHex = 0xFF6B705C
        ),
        Product(
            id = "p9",
            title = "Hydrating Serum & Skincare Kit",
            category = "Beauty",
            brandTagline = "GLOW CARE",
            priceInRupees = 1499,
            rating = 4.6,
            reviewCount = 88,
            imageUrl = "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=800&q=80",
            drawableRes = R.drawable.ic_prod_skincare,
            accentColorHex = 0xFFD0108A
        ),
        Product(
            id = "p10",
            title = "Wireless Mechanical Gaming Keyboard",
            category = "Gaming",
            brandTagline = "GAME ZONE",
            priceInRupees = 3899,
            rating = 4.9,
            reviewCount = 530,
            imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&q=80",
            drawableRes = R.drawable.ic_prod_keyboard,
            accentColorHex = 0xFF3A86EF
        ),
        Product(
            id = "p11",
            title = "Waterproof Travel Backpack",
            category = "Bags",
            brandTagline = "VOYAGE GEAR",
            priceInRupees = 2499,
            rating = 4.7,
            reviewCount = 210,
            imageUrl = "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800&q=80",
            drawableRes = R.drawable.ic_prod_backpack,
            accentColorHex = 0xFF2B2D42
        ),
        Product(
            id = "p12",
            title = "Yoga Mat & Fitness Resistance Band Set",
            category = "Sports & Fitness",
            brandTagline = "ACTIVE LIFE",
            priceInRupees = 1899,
            rating = 4.5,
            reviewCount = 145,
            imageUrl = "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=800&q=80",
            drawableRes = R.drawable.ic_prod_fitness,
            accentColorHex = 0xFF588157
        )
    )
}
