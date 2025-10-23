package com.example.androidtutorialhungeco.present

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorialhungeco.databinding.ActivityPaywallDefaultBinding

class PayWallActivity: AppCompatActivity(){

    private lateinit var binding: ActivityPaywallDefaultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ánh xạ layout bằng View Binding
        binding = ActivityPaywallDefaultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Thiết lập trạng thái ban đầu: not found
        setupInitialState()

        // Thiết lập lắng nghe sự kiện nhấn nút
        setClickListeners()

    }

    // Thiết lập lắng nghe sự kiện nhấn nút
    private fun setClickListeners() {
        // Sự kiện cho txtTryAgain: chuyển từ trạng thái "not found" sang "loading"
        binding.txtTryAgain.setOnClickListener {
            showLoadingState()

            // Thêm độ trễ để mô phỏng quá trình loading, sau đó chuyển sang trạng thái "offer"
            Handler(Looper.getMainLooper()).postDelayed({
                showOfferState()
            }, 3000) // delay = 3 giây cho loading
        }

        // Sự kiện cho btnClose (Nút đóng): có thể đóng Activity hoặc thực hiện hành động khác
        binding.btnClose.setOnClickListener {
            // Ví dụ: đóng Activity
            finish()
        }

        // Sự kiện cho btnClaimOffer (Nút yêu cầu ưu đãi): có thể bắt đầu quá trình thanh toán
        binding.btnClaimOffer.setOnClickListener {
        }
    }

    // thiết lập trạng thái ban đầu: not found
    private fun setupInitialState() {
        // Ẩn tất cả các phần tử khác để đảm bảo chỉ có not found hiển thị
        binding.viewSubContainer.visibility = View.GONE
        binding.btnLoading.visibility = View.GONE
        binding.txtPaymentDetails.visibility = View.GONE
        binding.btnClaimOffer.visibility = View.GONE
        binding.txtNewPrice.visibility = View.GONE
        binding.txtOFF50.visibility = View.GONE
        binding.txtWeek.visibility = View.GONE
        binding.txtPrice899.visibility = View.GONE
        binding.headerPrice.visibility = View.GONE

        // Hiển thị các phần tử của trạng thái "not found"
        binding.txtNotFound.visibility = View.VISIBLE
        binding.txtTryAgain.visibility = View.VISIBLE
    }

    // chuyển sang trạng thái loading
    private fun showLoadingState() {
        // Ẩn các phần tử của trạng thái trước đó (not found)
        binding.txtNotFound.visibility = View.GONE
        binding.txtTryAgain.visibility = View.GONE

        // Hiển thị các phần tử của trạng thái "loading"
        binding.viewSubContainer.visibility = View.VISIBLE
        binding.btnLoading.visibility = View.VISIBLE

    }

    // hiển thị đầy đủ thông tin (Offer State)
    private fun showOfferState() {
        // Ẩn các phần tử của trạng thái trước đó (loading)
        binding.viewSubContainer.visibility = View.GONE
        binding.btnLoading.visibility = View.GONE

        // Hiển thị các phần tử của trạng thái "offer"
        binding.txtPaymentDetails.visibility = View.VISIBLE
        binding.btnClaimOffer.visibility = View.VISIBLE
        binding.txtNewPrice.visibility = View.VISIBLE
        binding.txtOFF50.visibility = View.VISIBLE
        binding.txtWeek.visibility = View.VISIBLE
        binding.txtPrice899.visibility = View.VISIBLE

        binding.headerPrice.visibility = View.VISIBLE
    }
}