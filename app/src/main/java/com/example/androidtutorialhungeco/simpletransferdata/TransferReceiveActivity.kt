package com.example.androidtutorialhungeco.simpletransferdata

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorialhungeco.R
import com.example.androidtutorialhungeco.common.Constants
import com.example.androidtutorialhungeco.common.DataInformation

class TransferReceiveActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_receive)

        val tvReceiveContent = findViewById<TextView>(R.id.tvReceivedContent)
        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish() //về màn gửi
        }

        val intent = intent
        val output = StringBuilder()
        val receiverInt = intent.getIntExtra(Constants.KEY_INT, 0)
        output.append("Giá trị Int: $receiverInt\n\n")

        val receiArray = intent.getStringArrayExtra(Constants.KEY_STRING_ARRAY)
        output.append("mảng (array): ${receiArray?.joinToString()}\n\n")

        val receiveObject = intent.getSerializableExtra(Constants.KEY_OBJECT) as? DataInformation
        if (receiveObject != null){
            output.append("Đối tượng Object: \n")
            output.append("Tên: ${receiveObject.name}\n")
            output.append("Tuổi: ${receiveObject.age}\n")
        }else{
            output.append("NUll or sai kiểu")
        }
        tvReceiveContent.text = output.toString()
    }
}