package ru.get4click.sdk.ui.precheckout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import ru.get4click.sdk.R
import ru.get4click.sdk.data.models.precheckout.PreCheckoutItemData

internal class PreCheckoutAdapter(
    private val items: List<PreCheckoutItemData>,
    private val scope: CoroutineScope
) : RecyclerView.Adapter<PreCheckoutItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreCheckoutItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_precheckout, parent, false)

        return PreCheckoutItemViewHolder(view, scope)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PreCheckoutItemViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
