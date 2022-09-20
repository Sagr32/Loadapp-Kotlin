package com.sagr.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonText = ""
    private val valueAnimator = ValueAnimator()

    //    -----
    private var bgColor = 0
    private var fontColor = 0
    private var loadingColor = 0
    private var circleColor = 0

    //    -------------
     var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                Log.d("CustomView", "Clicked")

            }
            ButtonState.Loading -> {
                Log.d("CustomView", "Loading")

            }
            else -> {
                Log.d("CustomView", "Completed")

            }

        }
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
    }

    init {
        buttonText = getContext().getString(R.string.download)
        // set custom attributes for the view
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            bgColor = getColor(R.styleable.LoadingButton_bgColor, 0)
            fontColor = getColor(R.styleable.LoadingButton_fontColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loadingIndicatorColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawButtonBgColor(canvas)
            drawButtonTitle(canvas)

        }

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


    // Function to draw Title / Button Text
    private fun drawButtonTitle(canvas: Canvas) {
        paint.color = fontColor
        canvas.drawText(
            buttonText,
            (widthSize / 2).toFloat(),
            heightSize / 2 - (paint.descent() + paint.ascent()) / 2,
            paint
        )
    }

    // Function to set/draw Button background color
    private fun drawButtonBgColor(canvas: Canvas) {
        paint.color = bgColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
    }


}

