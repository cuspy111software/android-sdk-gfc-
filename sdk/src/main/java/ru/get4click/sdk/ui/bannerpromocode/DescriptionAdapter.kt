package ru.get4click.sdk.ui.bannerpromocode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.get4click.sdk.R
import ru.get4click.sdk.models.ItemDescriptionText

internal class DescriptionAdapter(
    initItems: List<ItemDescriptionText>
) : RecyclerView.Adapter<DescriptionItemViewHolder>() {

    var items: List<ItemDescriptionText> = initItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_text, parent, false)

        return DescriptionItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DescriptionItemViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
