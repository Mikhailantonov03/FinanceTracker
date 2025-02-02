**Finance Tracker** — это мобильное приложение для учета источников дохода и расходов. С помощью этого приложения пользователи могут легко отслеживать свои финансовые потоки, управлять бюджетом и анализировать свои доходы и расходы.

## Описание

Finance Tracker позволяет пользователю:
- Добавлять и редактировать источники дохода.
- Просматривать все добавленные источники дохода.
- Удалять источники дохода.
- Просматривать статистику по доходам и расходам.

Приложение использует **Room** для локального хранения данных и позволяет работать с асинхронными операциями через **Kotlin Coroutines** для повышения производительности.

### Ключевые особенности:
- **Сохранение данных**: Использует **Room** для хранения источников дохода в локальной базе данных.
- **Асинхронные операции**: Поддержка асинхронных операций с помощью **Kotlin Coroutines**.
- **Интерфейс на Compose**: UI компоненты построены с использованием **Jetpack Compose** для создания современного интерфейса.
- **Диаграммы и графики**: **Y-Charts** библиотека используется для отображения статистики и графиков по доходам.
- **Сериализация данных**: **Kotlinx Serialization** используется для работы с JSON-данными.
- **Сохранение настроек**: Для хранения пользовательских данных используется **DataStore**.
- **Экран загрузки**: **Splash Screen** реализован для отображения начальной загрузки приложения.
  
## Стек технологий

- **Kotlin** — основной язык разработки.
- **Jetpack Compose** — для создания UI компонентов.
- **Room** — для хранения данных в базе данных.
- **Kotlin Coroutines** — для асинхронных операций.
- **Hilt** — для внедрения зависимостей.
- **Flow** — для реактивной работы с данными.
- **Kotlinx Serialization** — для сериализации данных в формат JSON.
- **Y-Charts** — для отображения графиков и диаграмм.
- **DataStore** — для хранения пользовательских данных.
- **Splash Screen** — для загрузочного экрана.
