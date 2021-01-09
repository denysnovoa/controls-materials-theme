package com.example.materialTheme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.materialTheme.controls.Badge
import com.example.materialTheme.controls.Banner
import com.example.materialTheme.controls.MapMarker
import com.example.materialTheme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            bindChipGroupExtras()
            bindMarkerFavorite()
            bindBanner()
        }
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

    private fun ActivityMainBinding.bindMarkerFavorite() {
        markerLayout.addView(
            MapMarker.build(
                this@MainActivity,
                "498.000 €",
                R.style.MapMarker_Favorite
            )
        )
    }

    private fun ActivityMainBinding.bindBanner() {
        Banner.Builder(bannerFeedback)
            .withTitle(R.string.banner_title)
            .withMessage(R.string.banner_message)
            .withLeftButton(R.string.banner_cancel_button)
            .withRightButton(R.string.banner_ok_button)
            .showOk()
    }
}
