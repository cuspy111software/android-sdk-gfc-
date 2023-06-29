package ru.get4click.sdk.ui.bannerpromocode

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.get4click.sdk.R
import ru.get4click.sdk.databinding.ItemTextBinding
import ru.get4click.sdk.models.ItemDescriptionText
import ru.get4click.sdk.models.ItemDescriptionTextStyle

internal class DescriptionItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemTextBinding.bind(view)

    fun bind(itemText: ItemDescriptionText) {
        binding.text.text = itemText.text

        val resources = itemView.context.resources

        val marginDot = when (itemText.style) {
            ItemDescriptionTextStyle.Small -> {
                val textSize = resources.getDimensionPixelSize(R.dimen.text_size_xsmall)
                binding.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
                resources.getDimensionPixelSize(R.dimen.text_size_xsmall) / 2
            }
            ItemDescriptionTextStyle.Big -> {
                val textSize = resources.getDimensionPixelSize(R.dimen.text_size_big)
                binding.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
                resources.getDimensionPixelSize(R.dimen.text_size_big) / 2
            }
        }

        val params = binding.greenDot.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0, marginDot, 0, 0)
        binding.greenDot.layoutParams = params
    }
}
