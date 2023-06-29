package ru.get4click.sdk.ui.bannerpromocode

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import ru.get4click.sdk.R
import ru.get4click.sdk.databinding.PromoCodeBannerWithDescrLayoutBinding
import ru.get4click.sdk.models.BannerPromoCodeConfig
import ru.get4click.sdk.models.ItemDescriptionText
import ru.get4click.sdk.models.ItemDescriptionTextStyle
import ru.get4click.sdk.utils.copyTextToClipboard

internal class BannerPromoCodeWithDetailsDialog(
    context: Context,
    private val config: BannerPromoCodeConfig
) : BannerPromoCodeDialog(context) {
    private lateinit var binding: PromoCodeBannerWithDescrLayoutBinding

    private lateinit var adapter: DescriptionAdapter

    private val mainDescription = listOf(
        ItemDescriptionText(config.description, ItemDescriptionTextStyle.Big)
    )

    private val detailsDescription = listOfNotNull(
        ItemDescriptionText(config.description, ItemDescriptionTextStyle.Big),
        config.limitations?.let { ItemDescriptionText(it, ItemDescriptionTextStyle.Big) },
        config.restrictions?.let { ItemDescriptionText(it, ItemDescriptionTextStyle.Big) }
    )

    private var isMainMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PromoCodeBannerWithDescrLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWidthPercent()

        with(binding) {
            adapter = DescriptionAdapter(mainDescription)
            recyclerViewDescription.adapter = adapter

            textButtonMore.setOnClickListener { showDetails() }

            textButtonBottom.setOnClickListener {
                if (!isMainMode) {
                    showMain()
                }
            }

            buttonCopy.setOnClickListener {
                context.copyTextToClipboard(editTextPromoCode.text.toString())
                val toastText = context.getString(R.string.promo_code_saved_to_clipboard)
                Toast
                    .makeText(context, toastText, Toast.LENGTH_SHORT)
                    .show()
            }

            textTitle.text = config.title
            textDiscount.text = config.discountText
            textPromoCodeTitle.text = config.staticConfig.promoCodeTitle
                ?: context.getString(R.string.your_promo_code)

            config.staticConfig.topPartTextColor?.let(textTitle::setTextColor)
            config.staticConfig.topPartTextColor?.let(textPromoCodeTitle::setTextColor)
            config.staticConfig.primaryButtonTextColor?.let(buttonCopy::setTextColor)

            config.staticConfig.primaryButtonColor?.let(buttonCopy::setBackgroundColor)
            config.staticConfig.topPartColor?.let(bannerLayout::setBackgroundColor)

            editTextPromoCode.setText(config.promoCode)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMain() {
        binding.textButtonMore.isVisible = true
        binding.imageBack.isVisible = false
        binding.textButtonBottom.setText(R.string.promo_code_already_applied)
        adapter.items = mainDescription
        adapter.notifyDataSetChanged()
        isMainMode = true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDetails() {
        binding.textButtonMore.isVisible = false
        binding.imageBack.isVisible = true
        binding.textButtonBottom.setText(R.string.go_back)
        adapter.items = detailsDescription
        adapter.notifyDataSetChanged()
        isMainMode = false
    }

    private fun setWidthPercent() {
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
