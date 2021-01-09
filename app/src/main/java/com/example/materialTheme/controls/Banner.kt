package com.example.materialTheme.controls

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.materialTheme.R
import com.example.materialTheme.databinding.ViewBannerBinding
import com.example.materialTheme.extensions.getColorFromAttribute
import com.example.materialTheme.extensions.getDrawableCompat
import com.example.materialTheme.extensions.expand
import com.example.materialTheme.extensions.reduce

class Banner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), DialogInterface {

    private var lastShownTime: Long = 0
    var onDismiss: () -> Unit = {}

    private val binding = ViewBannerBinding.inflate(LayoutInflater.from(context), this)

    private var contentText: String?
        get() = binding.bannerContentTextView.text.toString()
        set(value) {
            binding.bannerContentTextView.text = value
        }

    private var titleText: String?
        get() = binding.bannerContentTittleTextView.text.toString()
        set(value) {
            binding.bannerContentTittleTextView.text = value
            binding.bannerContentTittleTextView.isGone = value == null
        }

    private var leftButtonText: String?
        get() = binding.bannerLeftButton.text.toString()
        set(value) {
            binding.bannerLeftButton.text = value
            binding.bannerLeftButton.isGone = value == null
        }

    private var rightButtonText: String?
        get() = binding.bannerRightButton.text.toString()
        set(value) {
            binding.bannerRightButton.text = value
            binding.bannerRightButton.isGone = value == null
        }

    private var iconDrawable: Drawable?
        get() = binding.bannerContentIconView.drawable
        set(value) {
            binding.bannerContentIconView.setImageDrawable(value)
            binding.bannerContentIconView.isGone = value == null
        }

    init {
        val padding = context.resources.getDimensionPixelSize(R.dimen.size8)
        this.setPadding(padding, padding, padding, padding)
    }

    // Meant to avoid multiple dismiss triggers because checking for animation & visibility is not enough
    private var dismissing = false

    override fun dismiss() {
        if (!isVisible || dismissing)
            return

        dismissing = true

        reduce(context, onFinish = onDismiss)
    }

    override fun cancel() {
        dismiss()
    }

    private fun show(hideAfter: Boolean = false) {
        if (isVisible)
            return

        dismissing = false

        if (animation == null || animation?.hasEnded() == true) {
            lastShownTime = System.currentTimeMillis()
            expand().apply {
                if (hideAfter) {
                    playSequentially(reduce(context, DELAY_MILLIS, onFinish = onDismiss))
                }
            }
        }
    }

    class Builder(private val banner: Banner) {

        private val context: Context
            get() = banner.context

        private var title: String? = null
        private var message: String? = null
        private var leftButtonAction: (() -> Unit)? = null
        private var leftButtonValue: String? = null
        private var rightButtonAction: ((dialog: DialogInterface) -> Unit)? = null
        private var rightButtonValue: String? = null
        private var icon: Drawable? = null

        fun withTitle(@StringRes title: Int): Builder {
            this.title = context.getString(title)
            return this
        }

        fun withMessage(@StringRes message: Int): Builder {
            this.message = context.getString(message)
            return this
        }

        fun withLeftButton(@StringRes leftButtonTextRes: Int, leftButtonAction: (() -> Unit)? = null): Builder {
            this.leftButtonAction = leftButtonAction
            leftButtonValue = context.getString(leftButtonTextRes)
            return this
        }

        fun withRightButton(@StringRes rightButtonTextRes: Int, rightButtonAction: ((dialog: DialogInterface) -> Unit)? = null): Builder {
            this.rightButtonAction = rightButtonAction
            rightButtonValue = context.getString(rightButtonTextRes)
            return this
        }

        fun withIcon(@DrawableRes iconDrawableRes: Int): Builder {
            icon = context.getDrawableCompat(iconDrawableRes)
            return this
        }

        fun show(hideAfter: Boolean = false) {
            banner.apply {
                titleText = title
                contentText = message
                binding.bannerLeftButton.setOnClickListener { leftButtonAction?.invoke() ?: dismiss() }
                leftButtonText = leftButtonValue
                binding.bannerRightButton.setOnClickListener { rightButtonAction?.invoke(banner) ?: dismiss() }
                rightButtonText = rightButtonValue
                iconDrawable = icon
            }
            banner.show(hideAfter)
        }

        fun showError(hideAfter: Boolean = false) {
            applyColor(R.attr.colorErrorSurface, R.attr.colorOnErrorSurface)
            show(hideAfter)
        }

        fun showOk(hideAfter: Boolean = false) {
            applyColor(R.attr.colorSuccessSurface, R.attr.colorOnSuccessSurface)
            show(hideAfter)
        }

        private fun applyColor(colorSurfaceId: Int, colorOnSurfaceId: Int) {
            banner.apply {
                background = context.getDrawableCompat(R.drawable.banner_border_background).apply {
                    setTint(context.getColorFromAttribute(colorSurfaceId))
                }
                context.getColorFromAttribute(colorOnSurfaceId).apply {
                    binding.bannerContentTextView.setTextColor(this)
                    binding.bannerContentTittleTextView.setTextColor(this)
                }
            }
        }

        fun onDismiss(action: () -> Unit): Builder {
            banner.onDismiss = action
            return this
        }
    }
}

private const val DELAY_MILLIS = 5000L
