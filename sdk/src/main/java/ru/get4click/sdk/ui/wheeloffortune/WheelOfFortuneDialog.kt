package ru.get4click.sdk.ui.wheeloffortune

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.StyleRes
import ru.get4click.sdk.databinding.WheelOfFortuneLayoutBinding
import ru.get4click.sdk.view.FortuneWheelView

/**
 * Example:
 * ```
 * // Init items list to show on the fortune of wheel
 * val items = listOf(...)
 * // Create the dialog
 * WheelOfFortuneDialog(context, R.style.Theme_NoTitleBar_Fullscreen, items)
 *      .apply {
 *          title = "Title"
 *          subtitle = "Subtitle text"
 *
 *          // Configure the fortune of wheel view
 *          wheelViewConfig = WheelOfFortuneConfig(
 *              borderStrokeWidth = 16F,
 *              borderStrokeColor = Color.GRAY,
 *              textSize = 40F,
 *              pivotCircleStrokeWidth = 8F,
 *              pivotCircleBorderColor = Color.YELLOW,
 *              pivotCircleFillingColor =  Color.BLUE
 *          )
 *      }
 *      .show()
 * ```
 *
 * @param context
 * @param themeResId layout theme; prefer to use full screen theme
 * @param items initial items to be displayed on the wheel of fortune
 */
class WheelOfFortuneDialog(
    context: Context,
    @StyleRes themeResId: Int,
    private val items: List<FortuneWheelView.Item>
) : Dialog(context, themeResId) {

    private lateinit var binding: WheelOfFortuneLayoutBinding

    var wheelViewConfig: WheelOfFortuneConfig? = null

    var title: String = ""

    var subtitle: String = ""

    init {
        require(items.isNotEmpty()) { "List [items] must not be empty" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WheelOfFortuneLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wheelViewConfig?.let { config ->
            binding.wheelOfFortune.borderWidth             = config.borderStrokeWidth
            binding.wheelOfFortune.borderColor             = config.borderStrokeColor
            binding.wheelOfFortune.textSize                = config.textSize
            binding.wheelOfFortune.pivotCircleStrokeWidth  = config.pivotCircleStrokeWidth
            binding.wheelOfFortune.pivotCircleBorderColor  = config.pivotCircleBorderColor
            binding.wheelOfFortune.pivotCircleFillingColor = config.pivotCircleFillingColor
        }

        binding.buttonCross.setOnClickListener { dismiss() }

        binding.textTitle.text = title
        binding.textSubtitle.text = subtitle
        binding.wheelOfFortune.items = items

        binding.buttonSpin.setOnClickListener {
            binding.wheelOfFortune.spinToPosition(4)
        }
    }
}
