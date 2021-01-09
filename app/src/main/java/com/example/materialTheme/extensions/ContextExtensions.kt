package com.example.materialTheme.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable =
    ContextCompat.getDrawable(this, drawableRes)!!

fun Context.getColorFromAttribute(colorAttrId: Int): Int {
    val typedValue = TypedValue()
    val theme: Resources.Theme = this.theme
    theme.resolveAttribute(colorAttrId, typedValue, true)

    return typedValue.data
}
