package ru.get4click.sdk

import android.R
import android.graphics.Color
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity


import ru.get4click.sdk.sample.databinding.ActivityMainBinding
import ru.get4click.sdk.ui.wheeloffortune.WheelOfFortuneConfig
import ru.get4click.sdk.ui.wheeloffortune.WheelOfFortuneDialog
import ru.get4click.sdk.view.FortuneWheelView

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WheelOfFortuneDialog(
            this,
            R.style.Theme_NoTitleBar_Fullscreen,
            listOf(
                FortuneWheelView.Item(
                    title = "Подарок",
                    color = Color.parseColor("#94BC35"),
                    textColor = Color.WHITE
                ),
                FortuneWheelView.Item(
                    title = "Пусто",
                    color = Color.parseColor("#FBEB00"),
                    textColor = Color.BLACK
                ),
                FortuneWheelView.Item(
                    title = "Скидка 15%",
                    color = Color.parseColor("#94BC35"),
                    textColor = Color.WHITE
                ),
                FortuneWheelView.Item(
                    title = "Бесплатная доставка",
                    color = Color.parseColor("#D4E0F5"),
                    textColor = Color.BLACK
                ),
                FortuneWheelView.Item(
                    title = "150 руб.",
                    color = Color.parseColor("#94BC35"),
                    textColor = Color.WHITE
                ),
                FortuneWheelView.Item(
                    title = "Скидка 5%",
                    color = Color.parseColor("#9B51E0"),
                    textColor = Color.BLACK
                ),
                FortuneWheelView.Item(
                    title = "500 руб.",
                    color = Color.parseColor("#94BC35"),
                    textColor = Color.BLACK
                ),
                FortuneWheelView.Item(
                    title = "Скидка 20%",
                    color = Color.parseColor("#4EBABF"),
                    textColor = Color.WHITE
                )
            )
        )
            .apply {
                title = "Испытайте удачу!"
                subtitle = "Оставьте ваш email и получите гарантированный приз!"
                wheelViewConfig = WheelOfFortuneConfig(
                    borderStrokeWidth = 16F,
                    borderStrokeColor = Color.GRAY,
                    textSize = 40F,
                    pivotCircleStrokeWidth = 8F,
                    pivotCircleBorderColor = Color.YELLOW,
                    pivotCircleFillingColor =  Color.BLUE
                )
            }
            .show()

    }
}