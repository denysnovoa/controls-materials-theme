package com.example.materialTheme.controls

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import com.example.materialTheme.R
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textview.MaterialTextView

class Badge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = R.style.Badge
) : MaterialTextView(context, attrs, defStyleAttr) {

    private val defaultHeight by lazy { resources.getDimensionPixelSize(R.dimen.size24) }

    private var textBadge: String?
        get() = text.toString()
        set(value) {
            text = value
            isGone = value == null
        }

    private var iconDrawableBadge: Drawable?
        get() = compoundDrawables[0]
        set(value) {
            setCompoundDrawablesWithIntrinsicBounds(value, null, null, null)
        }

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.Badge,
            defStyleAttr,
            defStyleRes
        ) {
            readAttributes()
            initBackground(context, attrs, defStyleAttr, defStyleRes)
        }
    }

    private fun TypedArray.readAttributes() {
        setIconDrawable()
        setLayoutParams()
    }

    private fun initBackground(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val background = background
        if (background != null && background !is ColorDrawable) {
            return
        }

        val shapeAppearanceModel = ShapeAppearanceModel.builder(
            context,
            attrs,
            defStyleAttr,
            defStyleRes
        ).setAllCorners(CornerFamily.ROUNDED, dpToPx(16).toFloat())
            .build()

        val materialDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = ColorStateList.valueOf(background.toBackgroundColor())
        }
        ViewCompat.setBackground(this, materialDrawable)
    }

    private fun Drawable?.toBackgroundColor(): Int {
        return if (this != null) {
            (this as ColorDrawable).color
        } else {
            Color.TRANSPARENT
        }
    }

    private fun TypedArray.setIconDrawable() {
        getDrawable(R.styleable.Badge_badgeIcon)?.let {
            val iconTint = getColor(R.styleable.Badge_badgeIconTint, Color.TRANSPARENT)
            if (iconTint != Color.TRANSPARENT) {
                it.setTint(iconTint)
            }
            iconDrawableBadge = it
        }
    }

    private fun TypedArray.setLayoutParams() {
        layoutParams = FrameLayout.LayoutParams(
            WRAP_CONTENT,
            getLayoutDimension(R.styleable.Badge_android_layout_height, defaultHeight)
        )
    }

    companion object {
        fun create(context: Context, text: String, @StyleRes defStyleRes: Int): Badge {
            return Badge(ContextThemeWrapper(context, defStyleRes), defStyleRes = defStyleRes).apply {
                this.textBadge = text
            }
        }
    }
}

fun View.dpToPx(dp: Int): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
