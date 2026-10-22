package com.example.omnie_commerceplatform.data

data class DeliveryAddress(
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val pincode: String = "",
    val addressType: String = "Home"
) {
    fun formattedAddress(): String {
        val details = listOf(street, city, state, pincode).filter { it.isNotBlank() }
        return if (details.isNotEmpty()) {
            "${details.joinToString(", ")} ($addressType)"
        } else {
            "No delivery address specified"
        }
    }
}

data class UserProfile(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: DeliveryAddress = DeliveryAddress()
)

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val totalAmount: Int,
    val deliveryAddress: DeliveryAddress,
    val contactPhone: String,
    val paymentMethod: String,
    val orderStatus: String = "Confirmed",
    val orderDate: String,
    val estimatedDelivery: String
)

data class LocationResult(
    val city: String,
    val state: String
)

object PincodeLookup {
    /**
     * Auto-detect City and State based on Indian 6-Digit PIN Code
     */
    fun lookupLocation(pincode: String): LocationResult? {
        val cleanPin = pincode.trim()
        if (cleanPin.length < 2) return null

        val prefix = cleanPin.take(2)
        return when (prefix) {
            "11" -> LocationResult("New Delhi", "Delhi")
            "12", "13" -> LocationResult("Gurugram", "Haryana")
            "14", "15", "16" -> LocationResult("Chandigarh", "Punjab")
            "17", "18", "19" -> LocationResult("Shimla", "Himachal Pradesh")
            "20", "21", "22", "23", "24", "25", "26", "27", "28" -> LocationResult("Lucknow", "Uttar Pradesh")
            "30", "31", "32", "33", "34" -> LocationResult("Jaipur", "Rajasthan")
            "36", "37", "38", "39" -> LocationResult("Ahmedabad", "Gujarat")
            "40" -> LocationResult("Mumbai", "Maharashtra")
            "41", "42", "43", "44" -> LocationResult("Pune", "Maharashtra")
            "45", "46", "47", "48" -> LocationResult("Indore", "Madhya Pradesh")
            "50" -> LocationResult("Hyderabad", "Telangana")
            "51", "52", "53" -> LocationResult("Visakhapatnam", "Andhra Pradesh")
            "56", "57", "58", "59" -> LocationResult("Bengaluru", "Karnataka")
            "60", "61", "62", "63", "64" -> LocationResult("Chennai", "Tamil Nadu")
            "67", "68", "69" -> LocationResult("Kochi", "Kerala")
            "70", "71", "72", "73", "74" -> LocationResult("Kolkata", "West Bengal")
            "75", "76", "77" -> LocationResult("Bhubaneswar", "Odisha")
            "78", "79" -> LocationResult("Guwahati", "Assam")
            "80", "81", "82", "83", "84", "85" -> LocationResult("Patna", "Bihar")
            else -> LocationResult("City Area", "India")
        }
    }

    /**
     * Reverse lookup PIN Code by City
     */
    fun lookupPincode(city: String): String? {
        val clean = city.trim().lowercase()
        return when {
            clean.contains("bengaluru") || clean.contains("bangalore") -> "560001"
            clean.contains("delhi") -> "110001"
            clean.contains("mumbai") || clean.contains("bombay") -> "400001"
            clean.contains("chennai") || clean.contains("madras") -> "600001"
            clean.contains("kolkata") || clean.contains("calcutta") -> "700001"
            clean.contains("hyderabad") -> "500001"
            clean.contains("pune") -> "410001"
            clean.contains("ahmedabad") -> "380001"
            clean.contains("jaipur") -> "300001"
            clean.contains("lucknow") -> "220001"
            clean.contains("chandigarh") -> "160001"
            clean.contains("kochi") || clean.contains("cochin") -> "680001"
            else -> null
        }
    }
}
