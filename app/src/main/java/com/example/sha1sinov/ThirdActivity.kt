package com.example.sha1sinov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sha1sinov.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {

    lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.text1.text = intent.extras!!.getString("two")

    }
}