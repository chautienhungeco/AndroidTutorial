package com.example.androidtutorialhungeco.common

sealed class DataPhone {
    data class Header(val letter: Char) : DataPhone() // Tiêu đề nhóm (A, B, C...)
    data class Contact(val name: String, val phone: String) : DataPhone() // Mục liên hệ
}
