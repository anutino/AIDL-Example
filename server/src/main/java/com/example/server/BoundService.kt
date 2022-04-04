package com.example.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.myapplication.Calculator

class BoundService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return object : Calculator.Stub() {
            override fun sum(first: Int, second: Int): Int {
                return first + second
            }
        }
    }
}