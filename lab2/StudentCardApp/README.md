# Карточка студента — JavaFX + Scene Builder (Вариант 1)

## Структура проекта
```
StudentCardApp/
├── pom.xml
└── src/main/
    ├── java/
    │   ├── module-info.java
    │   └── com/example/studentcard/
    │       ├── HelloApplication.java
    │       └── HelloController.java
    └── resources/com/example/studentcard/
        └── hello-view.fxml
```

## Как открыть в IntelliJ IDEA

1. **Установите JDK 17+**: File → Project Structure → SDK, если не установлен — скачайте (например, Amazon Corretto или Oracle JDK) прямо из окна выбора SDK.

2. **Установите Scene Builder** (отдельное приложение, не плагин):
   скачайте с https://gluonhq.com/products/scene-builder/ и установите.

3. **Распакуйте архив** `StudentCardApp.zip` в любую папку.

4. **Откройте проект**: IntelliJ IDEA → Open → выберите папку `StudentCardApp` (там, где лежит `pom.xml`). IntelliJ определит его как Maven-проект и сам подтянет зависимости JavaFX (нужен интернет при первой сборке).

5. **Подключите Scene Builder к IntelliJ**, чтобы двойной клик по `.fxml` открывал визуальный редактор:
   File → Settings → Languages & Frameworks → JavaFX → укажите путь к исполняемому файлу Scene Builder.
   После этого при открытии `hello-view.fxml` внизу экрана появится вкладка **Scene Builder**.

6. **Запуск программы** — два варианта:
   - Через Maven: откройте вкладку Maven справа → `StudentCardApp → Plugins → javafx → javafx:run` (двойной клик).
   - Через терминал в IntelliJ:
     ```
     mvn clean javafx:run
     ```
   - Если хотите запускать через зелёную кнопку ▶ у `main()`, нужно добавить в конфигурацию запуска VM options с путём к JavaFX SDK (если используете не-модульный classpath), либо просто запускать через `mvn javafx:run`, как выше — это проще всего.

7. **Редактирование интерфейса**: откройте `hello-view.fxml`, нажмите на вкладку Scene Builder внизу — увидите визуальное дерево VBox → Label → GridPane (поля) → HBox (кнопки) → Label результата. Можно перетаскивать новые компоненты и сразу задавать `fx:id` и `onAction` в панели Code справа.

## Что реализовано (Вариант 1 — «Карточка студента»)

- Поля ввода: Имя, Фамилия, Возраст, Специальность (TextField)
- ComboBox: Курс (1–4 курс)
- Кнопки: Сформировать, Очистить, Выход с обработчиками `onAction`
- Проверка ввода: пустые поля, возраст — число, диапазон 16–100 лет, ошибки выводятся через `Alert`

## Самостоятельная часть (раздел 9 задания)

- **ComboBox «Город»** — 5 городов (Алматы, Астана, Шымкент, Караганда, Актобе)
- **RadioButton «Очная / Дистанционная»** объединены в `ToggleGroup`
- **CheckBox «Проживает в общежитии»**
- Все значения выводятся в итоговой карточке результата (`lblResult`)
