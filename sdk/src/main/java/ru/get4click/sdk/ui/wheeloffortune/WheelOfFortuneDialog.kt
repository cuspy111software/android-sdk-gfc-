package ru.get4click.sdk.ui.wheeloffortune

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.StyleRes
import androidx.core.graphics.ColorUtils
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.get4click.sdk.R
import ru.get4click.sdk.data.models.Email
import ru.get4click.sdk.data.models.GiftId
import ru.get4click.sdk.databinding.WheelOfFortuneLayoutBinding
import ru.get4click.sdk.models.WofModalAppearance
import ru.get4click.sdk.utils.isEmail
import ru.get4click.sdk.view.FortuneWheelView

/**
 * @param context
 * @param themeResId layout theme; prefer to use full screen theme
 * @param wofModalAppearance the appearance of whole dialog screen Wheel Of Fortune
 * @param wofInteractor the communication bridge
 * @param isReady means is the wheel prepared to spin (gift id is received and known)
 */
internal class WheelOfFortuneDialog(
    context: Context,
    @StyleRes themeResId: Int,
    isReady: Boolean = false,
    private val wofModalAppearance: WofModalAppearance,
    private val wofInteractor: WheelOfFortuneDialogInteractor
) : Dialog(context, themeResId) {

    private lateinit var binding: WheelOfFortuneLayoutBinding

    private val dialogScope = CoroutineScope(SupervisorJob())

    private var readyToSpin: Boolean = isReady

    private val giftReadinessState = MutableStateFlow(GiftReadinessState())

    init {
       require(wofModalAppearance.wheelAppearance.items.isNotEmpty()) {
           "List [items] must not be empty"
       }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WheelOfFortuneLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSpin.isEnabled = readyToSpin

        with(wofModalAppearance) {
            binding.wheelOfFortune.borderColor = wheelAppearance.wheelColor
            binding.wheelOfFortune.pivotCircleBorderColor = Color.WHITE
            binding.wheelOfFortune.pivotCircleFillingColor = wheelAppearance.wheelColor
            binding.wheelOfFortune.borderColor

            binding.textTitle.text = title
            binding.textSubtitle.text = subtitle
            binding.buttonSpin.text = buttonText
            binding.buttonSpin.setTextColor(buttonTextColor)

            val colorPrimaryDisabled = ColorUtils.setAlphaComponent(buttonSpinColor, ALPHA)
            val commonColorState = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled)
                ),
                intArrayOf(buttonSpinColor, colorPrimaryDisabled)
            )
            binding.buttonSpin.backgroundTintList = commonColorState
            binding.checkBoxAgreement.buttonTintList = commonColorState
            binding.inputEmail.setBoxStrokeColorStateList(ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_focused)
                ),
                intArrayOf(buttonSpinColor, colorPrimaryDisabled, buttonSpinColor)
            ))
            binding.buttonClose.backgroundTintList = ColorStateList.valueOf(buttonGiftColor)
            binding.buttonClose.setTextColor(buttonTextColor)
            binding.layoutRoot.setBackgroundColor(modalBackgroundColor)
            binding.imageGiftLeft.imageTintList = ColorStateList.valueOf(leftGiftColor)
            binding.imageGiftRight.imageTintList = ColorStateList.valueOf(rightGiftColor)
            binding.wheelOfFortune.items = wheelAppearance.items.map {
                FortuneWheelView.Item(
                    title     = it.title,
                    color     = it.backgroundColor,
                    textColor = it.textColor
                )
            }
        }

        binding.editTextEmail.doAfterTextChanged { updateSpinButton() }

        binding.checkBoxAgreement.setOnCheckedChangeListener { _, isChecked ->
            binding.buttonSpin.isEnabled = isChecked
            updateSpinButton()
        }

        binding.buttonCross.setOnClickListener { dismiss() }
        binding.buttonClose.setOnClickListener { dismiss() }

        binding.buttonSpin.setOnClickListener {
            binding.buttonSpin.isEnabled = false
            binding.inputEmail.isEnabled = false
            binding.checkBoxAgreement.isEnabled = false

            dialogScope.launch(Dispatchers.IO) {
                val giftId = requestSpin()
                if (giftId != null) {
                    requestMakeDistribution(giftId)
                }
            }
        }

        dialogScope.launch {
            giftReadinessState
                .onEach { state ->
                    if (state.giftDistributionSucceed == true
                        && state.spinAnimationFinished == true) {

                        binding.layoutSpinWheel.isVisible = false
                        binding.layoutResult.isVisible = true
                        if (state.giftId?.id == 0) {
                            binding.textResultTitle.text = wofModalAppearance.loseTitle
                            binding.textResultSubtitle.text = wofModalAppearance.textAfterLose
                        } else if (state.giftId?.id != null) {
                            binding.textResultTitle.text = wofModalAppearance.winningTitle
                            binding.textResultSubtitle.text = wofModalAppearance.textAfterWin
                        }
                    }

                    if (state.giftDistributionSucceed == false
                        && state.spinAnimationFinished == true) {

                        showError()
                    }
                }
                .flowOn(Dispatchers.Main)
                .collect()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dialogScope.cancel()
    }

    fun setReady() {
        readyToSpin = true
        binding.textError.isInvisible = true
        updateSpinButton()
    }

    fun showError(errorText: String = context.getString(R.string.general_error_text)) {
        binding.textError.isInvisible = false
        binding.textError.text = errorText

        updateSpinButton()
    }

    private fun updateSpinButton() {
        binding.buttonSpin.isEnabled =
            binding.editTextEmail.text?.toString()?.isEmail() == true
            && binding.checkBoxAgreement.isChecked
            && readyToSpin
    }

    private suspend fun requestSpin(): GiftId? {
        val giftId = wofInteractor.spin().getOrNull()
        withContext(Dispatchers.Main) {
            binding.wheelOfFortune.spinToPosition(giftId?.id ?: 0) {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        giftReadinessState.value = giftReadinessState.value.copy(
                            spinAnimationFinished = true
                        )
                    },
                    DELAY_AFTER_SPIN_FINISHED
                )
            }
        }
        return giftId
    }

    private suspend fun requestMakeDistribution(giftId: GiftId) {
        val email = Email(binding.editTextEmail.editableText.toString())
        wofInteractor.makeDistribution(giftId, email)
            .onSuccess {
                giftReadinessState.value = giftReadinessState.value.copy(
                    giftDistributionSucceed = true,
                    giftId = giftId
                )
            }.onFailure {
                giftReadinessState.value = giftReadinessState.value.copy(
                    giftDistributionSucceed = false,
                    giftId = giftId
                )
            }
    }

    private data class GiftReadinessState(
        val spinAnimationFinished: Boolean? = null,
        val giftDistributionSucceed: Boolean? = null,
        val giftId: GiftId? = null
    ) {
        init {
            if (giftDistributionSucceed == true) {
                require(giftId != null) {
                    "Illegal state: [giftId] must not be null when gift distribution succeed"
                }
            }
        }
    }

    companion object {
        private const val DELAY_AFTER_SPIN_FINISHED = 2000L
        private const val ALPHA = 64
    }
}
