package com.example.androidtutorialhungeco.utils

import android.content.Context
import android.Manifest
import android.net.Uri
import androidx.lifecycle.LifecycleOwner

class PermissionHandle (
    private val owner: LifecycleOwner,
    private val context: Context,
    private val onPermissionGranted: (Boolean) -> Unit,
    private val anImageSeleted: (Uri) -> Unit
){
    private val TAG = "PermissionHandle"
    private val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

//    private val requestPermissionLauncher: ActivityResultLauncher<String> = owner.registerForResultActivity
}
