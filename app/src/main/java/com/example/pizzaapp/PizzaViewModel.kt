package com.example.pizzaapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class PizzaSize(val name: String, val price: Int)

class PizzaViewModel : ViewModel() {

    val sizes = listOf(
        PizzaSize("Small", 90_000),
        PizzaSize("Medium", 120_000),
        PizzaSize("Large", 150_000)
    )
    val toppings = listOf("Cheese", "Mushroom", "Sausage", "Olives")

    val toppingPrice = 15_000
    val deliveryFee = 20_000

    var customerName by mutableStateOf("")
        private set
    var selectedSize by mutableStateOf(sizes[1])
        private set
    val selectedToppings = mutableStateListOf<String>()
    var homeDelivery by mutableStateOf(false)
        private set

    val totalPrice: Int
        get() = selectedSize.price +
                selectedToppings.size * toppingPrice +
                (if (homeDelivery) deliveryFee else 0)

    val canPlaceOrder: Boolean
        get() = customerName.isNotBlank()

    fun onNameChange(name: String) {
        customerName = name
    }

    fun onSizeSelected(size: PizzaSize) {
        selectedSize = size
    }

    fun onToppingChecked(topping: String, checked: Boolean) {
        if (checked) selectedToppings.add(topping) else selectedToppings.remove(topping)
    }

    fun onDeliveryChange(enabled: Boolean) {
        homeDelivery = enabled
    }
}

fun formatPrice(price: Int): String = "%,d VND".format(price)
