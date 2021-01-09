package com.example.materialTheme.controls

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.ViewCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.TriangleEdgeTreatment
import com.example.materialTheme.R
import com.google.android.material.color.MaterialColors.*

class MapMarker @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.mapMarkerStyle,
    @StyleRes defStyleRes: Int = R.style.MapMarker
) : AppCompatCheckedTextView(context, attributeSet, defStyleAttr) {

    private var strokeWidth: Float = 2f

    private var pointSize: Int = 8.toDp()

    private var strokeColor: ColorStateList = ColorStateList.valueOf(
        getColor(this, R.attr.colorOnSurface)
    )

    private var fillColor: ColorStateList = ColorStateList.valueOf(
        getColor(this, R.attr.colorSurface)
    )

    private var rippleColor: ColorStateList = ColorStateList.valueOf(
        getColor(this, R.attr.colorOnPrimary)
    )

    init {
        context.withStyledAttributes(
            attributeSet,
            R.styleable.MapMarker,
            defStyleAttr,
            defStyleRes
        ) {
            readAttributes()
            initBackground(context, attributeSet, defStyleAttr, defStyleRes)
        }
    }

    private fun TypedArray.readAttributes() {
        pointSize = getDimensionPixelOffset(R.styleable.MapMarker_markerPointSize, pointSize)

        rippleColor = getColorStateList(
            R.styleable.MapMarker_markerRippleColor
        ) ?: rippleColor

        fillColor = getColorStateList(
            R.styleable.MapMarker_markerFillColor
        ) ?: fillColor

        strokeColor = getColorStateList(
            R.styleable.MapMarker_markerStrokeColor
        ) ?: strokeColor
    }

    private fun initBackground(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {

        val pointEdge = TriangleEdgeTreatment(pointSize.toFloat(), false)
        val shapeAppearanceModel = ShapeAppearanceModel.builder(
            context,
            attributeSet,
            defStyleAttr,
            defStyleRes
        ).setBottomEdge(pointEdge)
            .build()

        val materialDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        materialDrawable.initializeElevationOverlay(context)
        materialDrawable.elevation = ViewCompat.getElevation(this@MapMarker)

        materialDrawable.fillColor = fillColor

        materialDrawable.strokeWidth = strokeWidth
        materialDrawable.strokeColor = strokeColor

        materialDrawable.setPadding(
            paddingLeft,
            paddingTop,
            paddingRight,
            paddingBottom
        )

        val maskDrawable = MaterialShapeDrawable(shapeAppearanceModel)

        val rippleDrawable = RippleDrawable(
            rippleColor,
            wrapDrawableWithInset(materialDrawable),
            maskDrawable
        )

        ViewCompat.setBackground(this@MapMarker, rippleDrawable)
    }

    private fun wrapDrawableWithInset(drawable: Drawable): InsetDrawable {
        return InsetDrawable(drawable, 0.toDp(), 0.toDp(), 0.toDp(), pointSize)
    }

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
        MaterialShapeUtils.setElevation(this, elevation)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this)
    }

    companion object {

        fun build(context: Context, price: String, styleRes: Int): MapMarker {
            return MapMarker(ContextThemeWrapper(context, styleRes), defStyleRes = styleRes).apply {
                text = price
                measure(0, 0)
            }
        }

        fun MapMarker.asBitmap(): Bitmap {
            val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
            apply {
                layout(0, 0, this@asBitmap.measuredWidth, this@asBitmap.measuredHeight)
                draw(Canvas(bitmap))
            }
            return bitmap
        }
    }
}

fun Int.toDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

