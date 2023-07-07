package ru.get4click.sdk.ui.bannerpromocode

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import ru.get4click.sdk.R
import ru.get4click.sdk.databinding.PromoCodeBannerLayoutBinding
import ru.get4click.sdk.models.BannerPromoCodeConfig
import ru.get4click.sdk.utils.copyTextToClipboard

internal class SimpleBannerPromoCodeDialog(
    context: Context,
    private val config: BannerPromoCodeConfig
) : BannerPromoCodeDialog(context) {
    private lateinit var binding: PromoCodeBannerLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PromoCodeBannerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        adjustDialogWithPercents()

        with(binding) {
            textTitle.text = config.title
            textDiscount.text = config.discountText
            textDescription.text = config.description
            textPromoCodeTitle.text = config.staticConfig.promoCodeTitle
                ?: context.getString(R.string.your_promo_code)

            config.staticConfig.topPartTextColor?.let(textTitle::setTextColor)
            config.staticConfig.topPartTextColor?.let(textDescription::setTextColor)
            config.staticConfig.bottomPartTextColor?.let(textPromoCodeTitle::setTextColor)
            config.staticConfig.primaryButtonTextColor?.let(buttonCopy::setTextColor)

            config.staticConfig.primaryButtonColor?.let(buttonCopy::setBackgroundColor)
            config.staticConfig.topPartColor?.let(layoutTopPart::setBackgroundColor)
            config.staticConfig.bottomPartColor?.let(layoutBottomPart::setBackgroundColor)

            editTextPromoCode.setText(config.promoCode)

            buttonCopy.setOnClickListener {
                context.copyTextToClipboard(editTextPromoCode.text.toString())
                val toastText = context.getString(R.string.promo_code_saved_to_clipboard)
                Toast
                    .makeText(context, toastText, Toast.LENGTH_SHORT)
                    .show()
            }

            textButtonBottom.setOnClickListener { dismiss() }
        }
    }

    private fun adjustDialogWithPercents() {
        val percent = DIALOG_WIDTH_PERCENTS / 100F
        val displayMetrics = Resources.getSystem().displayMetrics
        val rect = displayMetrics.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent

        window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private const val DIALOG_WIDTH_PERCENTS = 90
    }
}
