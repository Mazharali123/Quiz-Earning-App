package com.example.quizapplication


import android.content.Intent
import android.os.Build.VERSION_CODES.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.example.quizapplication.databinding.ActivitySplashScreenBinding
import java.util.logging.Handler


class SplashScreen : AppCompatActivity() {

    private val binding : ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.textView.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}