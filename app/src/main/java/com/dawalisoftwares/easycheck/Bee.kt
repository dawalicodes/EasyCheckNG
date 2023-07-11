package com.dawalisoftwares.easycheck

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dawalisoftwares.easycheck.databinding.ActivityBeeBinding

class Bee : AppCompatActivity() {
    private lateinit var binding: ActivityBeeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}