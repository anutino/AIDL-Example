package com.example.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.Calculator

class MainActivity : AppCompatActivity() {

    private var mCalculator: Calculator? = null
    private lateinit var mFirstText : EditText
    private lateinit var mSecondText : EditText
    private lateinit var mResult : TextView
    private lateinit var mGetResultButton : Button

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mCalculator = Calculator.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            mCalculator = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirstText = findViewById(R.id.first)
        mSecondText = findViewById(R.id.second)
        mResult = findViewById(R.id.result)
        mGetResultButton = findViewById(R.id.get_result)

        mGetResultButton.setOnClickListener {
            mCalculator?.sum(Integer.parseInt(mFirstText.text.toString()),
                Integer.parseInt(mSecondText.text.toString()))?.let { it1 ->
                mResult.text = it1.toString()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(createExplicitIntent(), mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(mServiceConnection)
    }

    private fun createExplicitIntent(): Intent {
        val intent = Intent("com.example.aidl.REMOTE_CONNECTION")
        val services = packageManager.queryIntentServices(intent, 0)
        if (services.isEmpty()) {
            throw IllegalStateException("Приложение-сервер не установлено")
        }
        return Intent(intent).apply {
            val resolveInfo = services[0]
            val packageName = resolveInfo.serviceInfo.packageName
            val className = resolveInfo.serviceInfo.name
            component = ComponentName(packageName, className)
        }
    }
}