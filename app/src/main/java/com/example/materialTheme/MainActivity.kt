package com.example.materialTheme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.materialTheme.controls.Badge
import com.example.materialTheme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bindChipGroupExtras()
    }

    private fun ActivityMainBinding.bindChipGroupExtras() {
        val extras = listOf(
            "Extra 1",
            "Extra 2",
            "Extra 3",
            "Extra Salto de línea",
            "Extra 5",
            "Extra 6",
            "Extra 7",
            "Extra 8"
        )

        extras.forEach { extra ->
            Badge.create(
                this@MainActivity,
                extra,
                R.style.Badge
            ).also {
               chipGroupExtras.addView(it)
            }
        }
    }
}