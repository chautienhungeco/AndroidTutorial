package com.example.androidtutorial.mainui.profile

import android.content.pm.PackageManager
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.androidtutorial.R

class UserProfileFragment : Fragment() {
    private val TAG = "PROFILE_FRAGMENT"
    private lateinit var ivAvatar: ImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvHomeTown: TextView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGrandted: Boolean ->
        if (isGrandted) {
            Log.d(TAG, "Quyền truy cập thư viện ảnh được cấp")
            selectImageLauncher.launch("image/*")
        } else {
            Log.d(TAG, "Quyền truy cập thư viện ảnh bị từ chối")
            Toast.makeText(requireContext(), "Cần cấp quyền truy cập", Toast.LENGTH_LONG).show()
        }
    }

    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            ivAvatar.setImageURI(uri)
            updateUserInfo(
                "Hưng nè",
                "05/09/2002",
                "Lào Cai",
                uri
            )
        } else {
            Toast.makeText(requireContext(), "Chưa chọn ảnh nào", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUserInfo(fullName: String, date: String, homeTown: String, avatarUri: Uri?) {
        tvFullName.text = "Họ tên: $fullName"
        tvDate.text = "Ngày sinh: $date"
        tvHomeTown.text = "Quê quán: $homeTown"
        if (avatarUri != null) {
            ivAvatar.setImageURI(avatarUri)
        }
        Toast.makeText(requireContext(), "Đã thêm thông tin người dùng", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivAvatar = view.findViewById(R.id.ivAvatar)
        val btnSelectAvatar = view.findViewById<Button>(R.id.btnSelectAvatar)
        tvFullName = view.findViewById(R.id.tvFullName)
        tvDate = view.findViewById(R.id.tvDate)
        tvHomeTown = view.findViewById(R.id.tvHomeTown)
        val btnOpenSetting = view.findViewById<Button>(R.id.btnOpenSetting)

        updateUserInfo("[chưa cập nhật]", "[chưa cập nhật]", "[chưa cập nhật]", null)

        btnSelectAvatar.setOnClickListener {
            checkPermissionSelectImage()
        }

        btnOpenSetting.setOnClickListener {
            openAppSettings()
        }
    }

    private fun checkPermissionSelectImage() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val context = requireContext()

        when {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                selectImageLauncher.launch("image/*")
            }

            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(context, "Ứng dụng cần quyền truy cập", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(permission)
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }

        }
    }

    // ưứng dụng không hiển thị popup cấp quyền
    // mở cai đat xin cấp quyeen
    private fun openAppSettings() {
        val intent = Intent(
            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Không thể mở cài đặt ứng dụng.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
