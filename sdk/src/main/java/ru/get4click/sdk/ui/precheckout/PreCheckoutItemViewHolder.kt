package ru.get4click.sdk.ui.precheckout

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.get4click.sdk.data.models.precheckout.PreCheckoutItemData
import ru.get4click.sdk.databinding.ItemPrecheckoutBinding
import ru.get4click.sdk.utils.Image
import ru.get4click.sdk.utils.ImageDownloader

internal class PreCheckoutItemViewHolder(
    view: View,
    private val scope: CoroutineScope
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemPrecheckoutBinding.bind(view)

    fun bind(item: PreCheckoutItemData) {
        binding.textTitle.text = item.title
        binding.textDescription.text = item.description
        binding.textMore.isVisible = item.aboutUrl != null
        binding.textMore.isVisible = false

        scope.launch(Dispatchers.IO) {
            if (item.icon != null) {
                val icon = ImageDownloader.downloadInto(item.icon)
                withContext(Dispatchers.Main) {
                    if (icon is Image.BitmapImg) {
                        binding.icon.setImageBitmap(icon.bitmap)
                    } else {
                        binding.icon.isVisible = false
                    }
                }
            } else {
                binding.icon.isVisible = false
            }
        }
    }
}
