package com.example.androidtutorialhungeco.present

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorialhungeco.R
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.AppCompatImageView

class Dialog30Activity : AppCompatActivity() {

    // Khai báo các biến để tham chiếu đến các View
    private lateinit var txtNotFound30: AppCompatTextView
    private lateinit var txtTryAgain30: AppCompatTextView
    private lateinit var btnClaimLoading: AppCompatButton
    private lateinit var btnClaimOffer: AppCompatButton
    private lateinit var txtTrialOffer: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_sale_weekly)

        // 1. Ánh xạ các View từ layout
        txtNotFound30 = findViewById(R.id.txtNotFound30)
        txtTryAgain30 = findViewById(R.id.txtTryAgain30)
        btnClaimLoading = findViewById(R.id.btnClaimLoading)
        btnClaimOffer = findViewById(R.id.btnClaimOffer)
        txtTrialOffer = findViewById(R.id.txtTrialOffer)

        // 2. Thiết lập trạng thái ban đầu: hiện txtNotFound30 và txtTryAgain30, ẩn các cái còn lại.
        // *Lưu ý: btnClaimOffer và txtTrialOffer đã được set visibility="visible" trong XML,
        // nhưng chúng ta sẽ ẩn chúng ở đây để bắt đầu kịch bản.*
        btnClaimOffer.visibility = View.GONE
        txtTrialOffer.visibility = View.GONE

        txtNotFound30.visibility = View.VISIBLE
        txtTryAgain30.visibility = View.VISIBLE
        btnClaimLoading.visibility = View.GONE


        // 3. Thiết lập sự kiện click cho txtTryAgain30
        txtTryAgain30.setOnClickListener {
            // a. Ẩn txtNotFound30 và txtTryAgain30
            txtNotFound30.visibility = View.GONE
            txtTryAgain30.visibility = View.GONE

            // b. Hiện btnClaimLoading
            btnClaimLoading.visibility = View.VISIBLE

            // c. Đặt thời gian chờ 3 giây
            Handler(Looper.getMainLooper()).postDelayed({
                // d. Ẩn btnClaimLoading
                btnClaimLoading.visibility = View.GONE

                // e. Hiện btnClaimOffer và txtTrialOffer
                btnClaimOffer.visibility = View.VISIBLE
                txtTrialOffer.visibility = View.VISIBLE

                // *Tùy chọn: Có thể set lại sự kiện cho btnClaimOffer ở đây nếu cần.*
            }, 3000) // 3000 milliseconds = 3 giây
        }
    }
}