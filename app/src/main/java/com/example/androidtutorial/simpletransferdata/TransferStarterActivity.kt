package com.example.androidtutorial.simpletransferdata

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.R
import com.example.androidtutorial.common.Constants
import com.example.androidtutorial.common.DataInformation

class TransferStarterActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_starter)

        findViewById<Button>(R.id.btnSendAllData).setOnClickListener {
            val intent = Intent(this, TransferStarterActivity::class.java)
            intent.putExtra(Constants.KEY_INT, 592002)

            val skills = arrayOf("Bóng chuyển","Ngủ","Tịnh tâm")
            intent.putExtra(Constants.KEY_INT, skills)

            val internProfile = DataInformation(name = "Tiến Hưng", age = 23)
        }
    }
}