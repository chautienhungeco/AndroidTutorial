package com.example.androidtutorial.common

import java.io.Serializable

data class DataInformation(
    val name: String,
    val age: Int
) : Serializable