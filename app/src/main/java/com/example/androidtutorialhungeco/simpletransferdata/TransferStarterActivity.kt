package com.eco.musicplayer.audioplayer.music.simpletransferdata

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.common.Constants
import com.eco.musicplayer.audioplayer.music.common.DataInformation

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