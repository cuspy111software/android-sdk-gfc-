[![](https://jitpack.io/v/getforclick/Android-SDK.svg)](https://jitpack.io/#getforclick/Android-SDK)

# GET4Click SDK Android
Это SDK для Get4Click , язык Kotlin (Android )  в будущем релизе возможен переход на Kotlin Multiplatform

## Установка

Установка происходит через maven репозиторий

Сначала включите JitPack в  repositories list,

```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```

Чтобы импортировать Get4ClickSDK sdk импортируйте зависимость,

```groovy
dependencies {
  implementation 'com.github.cuspy111software:android-sdk-gfc-:1.1.2'
}
```

## Быстрый старт

В начале (перед созданием  заказа )инициализируйте SDK , методом initSDK(shopId : Int) задав ваш shopId

```kotlin
   Get4ClickSDK.initSDK(863)
```


Чтобы собрать параметры для заказа воспользуйтесь паттерном builder для обьекта order

```kotlin
    Get4ClickSDK.getCurrentOrder().
					setCustomerFirstName("name").
					setCustomerLastName("lastname")...

 ```

Возможные параметры для обьекта Order :


    Gender {
        male,
        female
    }

     customerFirstName: String
     customerLastName: String
     customerEmail: String
     customerPhone: String
     customerGender: Gender
     orderId: String
     orderValue: String
     orderCurrency: Currency
     usedPromoCode: String
     usedCategory : String




Есть возможность добавить несколько обьектов Order, для последующего выбора и вставки в BannerView


```kotlin
    Get4ClickSDK.addOrder("NewOne")
	
	 Get4ClickSDK.getOrder("NewOne").
					setCustomerFirstName("name").
					setCustomerLastName("lastname")...

 ```

Добавьте BannerView в разметку xml , или создайте программно

```xml
    <ru.get4click.sdk.view.BannerView
            android:layout_width="match_parent"
            android:id="@+id/bannerView"
            android:layout_height="150dp"/>

 ```
Создайте обьект Banner , передав ему bannerId  и Order

```kotlin
val banner = Banner(bannerId, order)
 ```

для удобства есть метод формирования обькета Banner для текущего Order в GET4ClickSDK.getBannerWithCurrentOrder(bannerId)


Вызовите метод showBanner(banner) у BannerView когда будете готовы

```kotlin
BannerView.showBanner(banner, 1.0)
 ```


У метода showBanner() есть не обязательный параметр scale : Double - можно уменьшить или увеличить баннер (по умолчанию 1.0)


Вызовите метод  Get4ClickSDK.resetCurrentOrder() для сброса текущего Order

```kotlin
Get4ClickSDK.resetCurrentOrder()
 ```

Полный код выглядит так :
```kotlin
		//Инициализация SDK 
        Get4ClickSDK.initSdk(863)


     	//Для сброса текущего заказа и начала нового
	    Get4ClickSDK.resetCurrentOrder()
		
		//Добавление полей к заказу
        Get4ClickSDK.getCurrentOrder().customerFirstName = "FirstName"
        Get4ClickSDK.getCurrentOrder().setCustomerLastName("LastName")
									  .setCustomerGender(Order.Gender.male)


        val bannerView: BannerView = findViewById(R.id.bannerView)
		
		//Создание модели баннера с Id  для текущего заказа 
        val banner = Get4ClickSDK.getBannerWithCurrentOrder(2804)
		//Показ баннера
        bannerView.showBanner(banner, 1.0)
		
	
 ```

## SDK Updates (1.2.0)

В версии 1.2.0 добавлено:
- Колесо фортуны (Wheel Of Fortune)
- Баннер промокод (Banner Promo Code)
- Crossmail

В SDK вся логика заключена в синглтон объекте `Get4ClickSDK`

### Колесо фортуны (Wheel Of Fortune)
Чтобы создать Колесо фортуны, проделываем следующие шаги:
1. Создаем объект `WheelOfFortune`
```kotlin
val wof = Get4ClickSDK.createWheelOfFortune(
    activity,
    "your_api_key",
    object : WheelOfFortuneListener {
        override fun onInit(wheelOfFortune: WheelOfFortune) {
            
        }

        override fun onInitFailed(e: Throwable) { 

        }

        override fun onShow() { 

        }

        override fun onShowFailed(e: Throwable) {

        }
    }
)
```
2. Когда объект Колеса фортуны создан, то необходимо время чтобы объект был проинициализирован и подготовлен к тому, чтобы отобразить колесо на экране. Для того, чтобы отслеживать такие события (события инициализации и показа), SDK предоставляет `WheelOfFortuneListener` с соответствующими колбэками. Отобразить колесо на экране можно только в том случае, когда колесо проинициализировано - вызван коллбэк `onInit`. Тогда, чтобы отобразить колесо сразу, как только оно будет готово, нужно сделать следующим образом:
```kotlin
val wof = Get4ClickSDK.createWheelOfFortune(
    activity,
    "your_api_key",
    object : WheelOfFortuneListener {
        override fun onInit(wheelOfFortune: WheelOfFortune) {
            wheelOfFortune.show()
        }
    }
)
```
Также, SDK предлагает виджет кнопки по умолчанию для показа Колеса фортуны. Как только колесо проинициализировано, можно получить виджет ImageView следующим образом:
```kotlin
override fun onInit(wheelOfFortune: WheelOfFortune) {
    val button = wheelOfFortune.getButton()
    binding.root.addView(button) // Добавляем виджет на лэйаут
}
```
Если пользователь SDK не хочет использовать кнопку по умолчанию, то можно отобразить колесо фортуны при возникновении любого друго события:
```kotlin
yourButton.isEnabled = false

val wof = Get4ClickSDK.createWheelOfFortune(
    activity,
    "your_api_key",
    object : WheelOfFortuneListener {
        override fun onInit(wheelOfFortune: WheelOfFortune) {
            yourButton.isEnabled = true
        }
    }
)

yourButton.setOnClickListener { 
    wof.show()
}
```
### Баннер промокод (Banner Promo Code)
Чтобы создать Баннер промокод, проделываем следующие шаги:
#### 1. Создаем объект `BannerPromoCode`:
```kotlin
val banner = Get4ClickSDK.createSimpleBannerPromoCode(
    activity = activity,
    apiKey = "your_api_key",
    email = "youremail@mail.com",
    viewType = BannerPromoCodeViewType.Expandable,
    config = BannerPromoCodeStaticConfig(),
    promoCodeListener = object : BannerPromoCodeListener {
        override fun onInit(bannerPromoCode: BannerPromoCode) {
            
        }

        override fun onInitFailed(e: Throwable) { 

        }
    }
)
```
Существует 2 типа баннера:
- Обычный `BannerPromoCodeViewType.Simple` - Простая реализация. Описание промокода в виде текста с абзацами.
- Расширяемый `BannerPromoCodeViewType.Expandable` - Описание промокода в виде пунктов. Если количетво пунктов больше чем 1, то отображается кнопка "Подробнее", при нажатии на которую отображается остальная часть описания.
  Кроме того, стиль баннера можно кастомизировать. Кастомизация осуществляется с помощью `BannerPromoCodeStaticConfig` (документация прилагается в виде javadoc).
#### 2. Показазываем баннер
Чтобы показать баннер, он должен быть обязательно проинициализирован (вызывается коллбэк `onInit`). Для отображения баннер используется метод `banner.show()`:
```kotlin
val banner = Get4ClickSDK.createSimpleBannerPromoCode(
    activity = activity,
    apiKey = "your_api_key",
    email = "youremail@mail.com",
    viewType = BannerPromoCodeViewType.Expandable,
    config = BannerPromoCodeStaticConfig(),
    promoCodeListener = object : BannerPromoCodeListener {
        override fun onInit(bannerPromoCode: BannerPromoCode) {
            bannerPromoCode.show()
        }

        override fun onInitFailed(e: Throwable) { }
    }
)
```

Кроме того, API баннер предлагает кнопку по умолчанию, которая уже настроена для работы с баннером:
```kotlin
val banner = Get4ClickSDK.createSimpleBannerPromoCode(
    activity = activity,
    apiKey = "your_api_key",
    email = "youremail@mail.com",
    viewType = BannerPromoCodeViewType.Expandable,
    config = BannerPromoCodeStaticConfig(),
    promoCodeListener = object : BannerPromoCodeListener {
        override fun onInit(bannerPromoCode: BannerPromoCode) {
            // Получаем виджет кнопки и добавляем его в корневой лэйаут
            bannerPromoCode.getButton()?.let { binding.root.addView(it) }
        }

        override fun onInitFailed(e: Throwable) { }
    }
)
```
### Crossmail
Crossmail используется для того, чтобы отслеживать статус пользователя, зная его email. Чтобы отправить статус пользователя делаем следующее:
```kotlin
val crossMail = Get4ClickSDK.getCrossMail("your_api_key")
buttonCrossMail.setOnClickListener {
    crossMail.sendStatus(
        email = "youremail@mail.com", 
        status = 1, // Статус в виде целочисленного значения
        // resultListener не обязательный параметр
        resultListener = object : ResultListener {
            override fun onSuccess() {
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(e: Throwable) {
                Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show()
            }
        }
    )
}
```
В SDK предопределены некоторые статусы с помощью перечисления enum `CrossMailStatus`:
```kotlin
crossMail.sendStatus(
    email = "youremail@mail.com", 
    status = CrossMailStatus.EnterApp // Либо CrossMailStatus.Order
)
```