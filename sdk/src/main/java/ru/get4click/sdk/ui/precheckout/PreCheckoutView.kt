package ru.get4click.sdk.ui.precheckout

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.ContentViewCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import ru.get4click.sdk.databinding.PrecheckoutLayoutBinding
import ru.get4click.sdk.models.PreCheckoutModel

@SuppressLint("ViewConstructor")
internal class PreCheckoutView(
    context: Context,
    private val preCheckoutModel: PreCheckoutModel
) : FrameLayout(context), ContentViewCallback {

    private lateinit var adapter: PreCheckoutAdapter

    private val binding = PrecheckoutLayoutBinding.inflate(LayoutInflater.from(context))

    private val viewScope = CoroutineScope(SupervisorJob())

    var onCloseClicked: () -> Unit = { }

    init {
        addView(binding.root)
        initButtonClose()
        initAdapter()
    }

    override fun animateContentIn(delay: Int, duration: Int) { /* no-op */ }

    override fun animateContentOut(delay: Int, duration: Int) { /* no-op */ }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewScope.cancel()
    }

    private fun initButtonClose() {
        binding.buttonClose.setOnClickListener {
            onCloseClicked()
        }
    }

    private fun initAdapter() {
        adapter = PreCheckoutAdapter(preCheckoutModel.messages, viewScope)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )
        binding.recyclerView.adapter = adapter
    }
}