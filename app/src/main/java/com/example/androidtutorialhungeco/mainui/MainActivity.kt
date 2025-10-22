package com.example.androidtutorialhungeco.mainui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.androidtutorialhungeco.R
import com.example.androidtutorialhungeco.launchmode.SingleInstanceActivity
import com.example.androidtutorialhungeco.launchmode.SingleTaskActivity
import com.example.androidtutorialhungeco.launchmode.SingleTopActivity
import com.example.androidtutorialhungeco.launchmode.StandardActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity(this, "Màn Launch Mode")
    }
}

fun setupActivity(activity: AppCompatActivity, title: String) {
    activity.setContentView(R.layout.activity_base)
    val tvTitle: TextView = activity.findViewById(R.id.tvTitle)
    val tvInfo: TextView = activity.findViewById(R.id.tvInfo)

    tvTitle.text = title
    tvInfo.text = "ID task: ${activity.taskId}\n Instance ID: ${activity.hashCode()}"

    Log.d("LAUNCH MODE", "$title -> TaskID: ${activity.taskId}, InstanceID: ${activity.hashCode()}")

    activity.findViewById<Button>(R.id.btnGoToStandard).setOnClickListener {
        activity.startActivity(Intent(activity, StandardActivity::class.java))
    }

    activity.findViewById<Button>(R.id.btnGoToSingleTop).setOnClickListener {
        activity.startActivity(Intent(activity, SingleTopActivity::class.java))
    }

    activity.findViewById<Button>(R.id.btnGoToSingleTask).setOnClickListener {
        activity.startActivity(Intent(activity, SingleTaskActivity::class.java))
    }

    activity.findViewById<Button>(R.id.btnGoToSingleInstance).setOnClickListener {
        activity.startActivity(Intent(activity, SingleInstanceActivity::class.java))
    }

    activity.findViewById<Button>(R.id.btnFinish).setOnClickListener {
        activity.finish()
    }
}
