package ru.get4click.sdk.view

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Color.parseColor
import android.graphics.Shader.TileMode
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.withRotation
import androidx.core.graphics.withSave
import ru.get4click.sdk.R
import ru.get4click.sdk.utils.dpToPx
import ru.get4click.sdk.utils.spToPx
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

internal class FortuneWheelView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val sectorsList: MutableList<Sector> = mutableListOf()

    private var _items = mutableListOf<Item>()

    private val sectPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = DEFAULT_BOARDER_COLOR
    }

    private val pivotCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.WHITE
    }

    private val filledCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = DEFAULT_BOARDER_COLOR
    }

    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = DEFAULT_BOARDER_COLOR
        strokeWidth = DEFAULT_ARROW_STROKE_WIDTH
        pathEffect = CornerPathEffect(ARROW_CORNER_RADIUS)
    }

    private val arrowInsidePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
        pathEffect = CornerPathEffect(ARROW_CORNER_RADIUS)
    }

    private val arrowPath = Path()

    private val textPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private var rotationAnimValue: Float = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    /**
     *  Items displayed on the wheel
     */
    var items: List<Item>
        get() = _items
        set(value) {
            _items = value.toMutableList()
            invalidate()
        }

    /**
     * The radius of the circle
     */
    var radius: Float = 0F
        set(value) {
            field = value
            if (sectorsList.isNotEmpty())
                refreshSectors()
            invalidate()
        }

    /**
     * Text size in pixels
     */
    var textSize: Float = 0F
        set(value) {
            field = value
            textPaint.textSize = value
            invalidate()
        }

    /**
     * The width of the border around the wheel (in pixels)
     */
    var borderWidth: Float = DEFAULT_BORDER_STROKE_WIDTH
        set(value) {
            field = value
            invalidate()
        }

    /**
     * The stroke width of the pivot inside the wheel (in pixels)
     */
    var pivotCircleStrokeWidth: Float = DEFAULT_BORDER_STROKE_WIDTH
        set(value) {
            field = value
            invalidate()
        }

    /**
     * The color of the border around the wheel
     */
    @ColorInt
    var borderColor: Int = DEFAULT_BOARDER_COLOR
        set(value) {
            field = value
            borderPaint.color = value
            arrowPaint.color = value
            invalidate()
        }

    /**
     * The color of the circle border of the pivot in the center of the wheel
     */
    @ColorInt
    var pivotCircleBorderColor: Int = DEFAULT_BOARDER_COLOR
        set(value) {
            field = value
            pivotCirclePaint.color = value
            invalidate()
        }

    /**
     * The color of the circle filling of the pivot in the center of the wheel
     */
    @ColorInt
    var pivotCircleFillingColor: Int = Color.WHITE
        set(value) {
            field = value
            filledCirclePaint.color = value
            invalidate()
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.FortuneWheelView,
            0,
            0
        ).apply {
            // Receive attributes
            try {
                textSize = spToPx(
                    getDimension(R.styleable.FortuneWheelView_text_size, DEFAULT_TEXT_SIZE_SP),
                    resources
                )

                borderColor = getColor(
                    R.styleable.FortuneWheelView_border_color,
                    DEFAULT_BOARDER_COLOR
                )

                borderWidth = dpToPx(
                    getDimension(
                        R.styleable.FortuneWheelView_border_width,
                        DEFAULT_BORDER_STROKE_WIDTH
                    ),
                    resources
                )

                pivotCircleStrokeWidth = dpToPx(
                    getDimension(
                        R.styleable.FortuneWheelView_pivot_circle_width,
                        DEFAULT_BORDER_STROKE_WIDTH
                    ),
                    resources
                )

                textPaint.textSize = this@FortuneWheelView.textSize

                borderPaint.strokeWidth = borderWidth
                pivotCirclePaint.strokeWidth = pivotCircleStrokeWidth
            } finally {
                recycle()
            }
        }
    }

    /**
     * Dynamically adds new [Item]. The [item] is immediately displayed on the canvas
     */
    fun addItem(item: Item) {
        if (sectorsList.isNotEmpty()) {
            _items.add(item)
            sectorsList.add(createSector(getSectorCenter(_items.size - 1), item))
            refreshSectors()
            invalidate()
        }
    }

    /**
     * @param position the position to spin
     * @param spinDuration duration of spinning animation
     * @param loopsBeforeStop how many loops to spin before stopping
     */
    fun spinToPosition(
        position: Int,
        spinDuration: Long = DEFAULT_SPIN_DURATION,
        loopsBeforeStop: Int = SPINS_BEFORE_STOP,
        onSpinComplete: () -> Unit = { }
    ) {
        val segmentDegrees = 360F / items.size
        val destinationRotation = 360F * (loopsBeforeStop + 1) - segmentDegrees * (position + 0.5F)
        
        ObjectAnimator.ofFloat(this, null, 0F, destinationRotation)
            .apply {
                addUpdateListener { rotationAnimValue = it.animatedValue as Float }
                addListener(object : AnimatorListener {
                    override fun onAnimationStart(animation: Animator) { /* no-op */ }
                    override fun onAnimationCancel(animation: Animator) { /* no-op */ }
                    override fun onAnimationRepeat(animation: Animator) { /* no-op */ }

                    override fun onAnimationEnd(animation: Animator) {
                        onSpinComplete.invoke()
                    }
                })
                duration = spinDuration
                interpolator = DecelerateInterpolator()
            }.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        var currAngle = 0F

        // Animate rotation with custom [rotationAnimValue] property because arrow must be
        // stationary
        canvas.withRotation(rotationAnimValue, pivotX, pivotY) {
            sectorsList.forEach { sect ->
                val sweepAngle = FULL_CIR_DEGREES / _items.size
                val finalAngle = currAngle + sweepAngle

                // Draw a sector
                canvas.drawArc(
                    sect.circle,
                    currAngle,
                    sweepAngle,
                    true,
                    sectPaint.apply { shader = sect.radialGradient }
                )

                // Draw the pivot circle inside the wheel
                canvas.drawCircle(pivotX, pivotY, radius * 0.1F, filledCirclePaint)
                canvas.drawCircle(pivotX, pivotY, radius * 0.1F, pivotCirclePaint)

                // Draw a title on a sector
                canvas.withSave {
                    rotate(
                        currAngle + sweepAngle / 2,
                        sect.title.x,
                        sect.title.y,
                    )

                    translate(sect.title.x, sect.title.y - sect.titleStaticLayout.height / 2)

                    sect.titleStaticLayout.paint.color = sect.title.color
                    sect.titleStaticLayout.draw(canvas)
                }

                currAngle = finalAngle
            }

            // Draw the border around the wheel
            canvas.drawCircle(
                pivotX,
                pivotY,
                radius + borderWidth / 2,
                borderPaint
            )
        }

        // Draw arrow borders
        val arrowTop = pivotY - radius - borderWidth
        with(arrowPath) {
            reset()
            fillType = Path.FillType.EVEN_ODD
            moveTo(width / 2F - radius * 0.1F, arrowTop)
            lineTo(width / 2F + radius * 0.1F, arrowTop)
            lineTo( width / 2F, arrowTop + radius * 0.2F)
            lineTo( width / 2F - radius * 0.1F, arrowTop)
            close()
        }

        canvas.drawPath(
            arrowPath,
            arrowPaint.apply { setShadowLayer(5F, 0F, 0F, Color.WHITE) }
        )

        // Draw arrow white triangle inside
        with(arrowPath) {
            val stroke = arrowPaint.strokeWidth
            reset()
            fillType = Path.FillType.EVEN_ODD
            moveTo(width / 2F - radius * 0.1F + stroke, arrowTop + stroke)
            lineTo(width / 2F + radius * 0.1F - stroke, arrowTop + stroke)
            lineTo( width / 2F, arrowTop + radius * 0.2F - stroke)
            lineTo( width / 2F - radius * 0.1F + stroke, arrowTop + stroke)
            close()
        }

        canvas.drawPath(
            arrowPath,
            arrowInsidePaint
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        initSectorsList()
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Calculates width considering the radius, paddings and border width
        radius = (min(w, h) - paddingRight - paddingLeft) / 2F - borderWidth
    }

    @Suppress("DEPRECATION")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Min layout width to place a circle with paddings
        val minWidth = paddingLeft + paddingRight + (MIN_RADIUS_PX + borderWidth) * 2

        val w = resolveSize(minWidth.toInt(), widthMeasureSpec)
        val h = resolveSize(minWidth.toInt(), heightMeasureSpec)

        var size =
            when {
                h > minWidth && w > minWidth -> min(w, h)
                h <= minWidth && w > minWidth -> w
                h > minWidth && w <= minWidth -> h
                else -> MIN_RADIUS_PX
            }

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val maxSize = min(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)

        if (size > maxSize) {
            size = maxSize
        }

        setMeasuredDimension(size, size)
    }

    private fun initSectorsList() {
        sectorsList.clear()
        _items.forEachIndexed { i, it ->
            val center = getSectorCenter(i)
            sectorsList.add(createSector(center, it))
        }
    }

    @Suppress("DEPRECATION")
    private fun createSector(center: Pair<Float, Float>, item: Item): Sector {
        return Sector(
            circle = RectF(
                pivotX - radius,
                pivotY - radius,
                pivotX + radius,
                pivotY + radius
            ),
            color = item.color,
            title = createTitle(center, item),
            radialGradient = RadialGradient(
                pivotX,
                pivotY,
                radius,
                createFourStepsColorGradient(item.color),
                RADIAL_GRADIENT_STOPS,
                TileMode.CLAMP
            ),
            titleStaticLayout = StaticLayout(
                item.title,
                textPaint.apply { color = item.textColor },
                (radius * TEXT_SPACE_RATIO).toInt(),
                Layout.Alignment.ALIGN_NORMAL,
                1F,
                0F,
                false
            )
        )
    }

    // Call it if some size parameter is changed (e.g. items amount, radius)
    private fun refreshSectors() {
        _items.forEachIndexed { i, _ ->
            sectorsList[i] = sectorsList[i].copy(
                circle = RectF(
                    pivotX - radius,
                    pivotY - radius,
                    pivotX + radius,
                    pivotY + radius
                )
            )
        }
    }

    // Returns (x, y) coordinates of the center of a n-th sector
    private fun getSectorCenter(sectorNum: Int): Pair<Float, Float> {
        val degrees = FULL_CIR_DEGREES / _items.size * sectorNum +
                FULL_CIR_DEGREES / _items.size / 2

        val sinA = sin(degrees * PI / HALF_CIR_DEGREES)
        val cosA = cos(degrees * PI / HALF_CIR_DEGREES)
        val resX = pivotX + (radius * ICON_RELATIVE_START_POS) * cosA
        val resY = pivotY + (radius * ICON_RELATIVE_START_POS) * sinA
        return Pair(resX.toFloat(), resY.toFloat())
    }

    private fun createTitle(
        center: Pair<Float, Float>,
        item: Item
    ): Title {
        return Title(
            x = center.first,
            y = center.second,
            text = item.title,
            color = item.textColor
        )
    }

    private fun createFourStepsColorGradient(@ColorInt mainColor: Int): IntArray {
        val lighterColor = changeColorLuminance(mainColor, INCREASE_LUMINANCE_RATIO)
        return intArrayOf(mainColor, mainColor, lighterColor, mainColor)
    }

    @Suppress("SameParameterValue")
    private fun changeColorLuminance(color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        ColorUtils.colorToHSL(color, hsv)
        hsv[2] *= factor // Adjust the luminance value

        return ColorUtils.HSLToColor(hsv)
    }

    private data class Sector(
        val circle: RectF,
        val title: Title,
        @ColorInt val color: Int,
        val radialGradient: RadialGradient,
        val titleStaticLayout: StaticLayout
    )

    private data class Title(
        val x: Float,
        val y: Float,
        val text: String,
        @ColorInt val color: Int
    )

    data class Item(
        val title: String,
        @ColorInt val color: Int,
        @ColorInt val textColor: Int
    )

    companion object {
        private const val MIN_RADIUS_PX = 300
        private const val FULL_CIR_DEGREES = 360F
        private const val HALF_CIR_DEGREES = 180F
        private const val ICON_RELATIVE_START_POS = 0.3F
        private const val DEFAULT_BORDER_STROKE_WIDTH = 8F
        private const val DEFAULT_TEXT_SIZE_SP = 8F
        private const val TEXT_SPACE_RATIO = 0.7F // Means that a text takes 70% of a sector radius
        private const val INCREASE_LUMINANCE_RATIO = 1.2F
        private const val SPINS_BEFORE_STOP = 6
        private const val DEFAULT_SPIN_DURATION = 4000L
        private const val ARROW_CORNER_RADIUS = 8F
        private const val DEFAULT_ARROW_STROKE_WIDTH = 16F
        private val DEFAULT_BOARDER_COLOR = parseColor("#76962A")
        private val RADIAL_GRADIENT_STOPS = floatArrayOf(0F, 0.75F, 0.95F, 0.99F)
    }
}