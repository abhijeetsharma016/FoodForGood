package com.example.foodforgood.model

data class cartItems(
    var foodName: String ?= null,
    var foodPrice: String ?= null,
    var foodDescription: String ?= null,
    var foodImage: String ?= null,
    var foodQuantity: Int ?= null,
    var foodIngredient: String ?= null
)