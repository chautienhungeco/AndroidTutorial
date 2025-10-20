package com.example.androidtutorial.utils

import android.content.Context
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope

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
