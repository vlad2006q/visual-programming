package kz.atu.lab12.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.atu.lab12.model.Movie;
import kz.atu.lab12.repository.MovieRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieController {

    @FXML private TableView<Movie> tableMovies;
    @FXML private TableColumn<Movie, Number> colId;
    @FXML private TableColumn<Movie, String> colTitle;
    @FXML private TableColumn<Movie, String> colGenre;
    @FXML private TableColumn<Movie, Number> colRating;

    @FXML private TextField txtTitle;
    @FXML private ComboBox<String> cmbGenre1;
    @FXML private ComboBox<String> cmbGenre2;
    @FXML private TextField txtRating;
    @FXML private TextField txtSearch;
    @FXML private Label lblStatus;

    private final MovieRepository repository = new MovieRepository();
    private final ObservableList<Movie> items = FXCollections.observableArrayList();

    private static final String NO_SECOND_GENRE = "(нет)";

    @FXML
    private void initialize() {
        colId.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().id()));
        colTitle.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().title()));
        colGenre.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().genre()));
        colRating.setCellValueFactory(d -> new ReadOnlyObjectWrapper<>(d.getValue().rating()));

        // Расширенный список жанров
        List<String> genres = List.of(
                "Аниме",
                "Биография",
                "Боевик",
                "Вестерн",
                "Военный",
                "Детектив",
                "Документальный",
                "Драма",
                "История",
                "Комедия",
                "Криминал",
                "Мелодрама",
                "Мультфильм",
                "Мюзикл",
                "Приключения",
                "Семейный",
                "Спорт",
                "Триллер",
                "Ужасы",
                "Фантастика",
                "Фэнтези"
        );

        cmbGenre1.setItems(FXCollections.observableArrayList(genres));

        List<String> secondGenres = new ArrayList<>();
        secondGenres.add(NO_SECOND_GENRE);
        secondGenres.addAll(genres);
        cmbGenre2.setItems(FXCollections.observableArrayList(secondGenres));
        cmbGenre2.setValue(NO_SECOND_GENRE);

        // Поиск по названию
        FilteredList<Movie> filtered = new FilteredList<>(items, m -> true);
        tableMovies.setItems(filtered);

        txtSearch.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV.trim().toLowerCase();
            filtered.setPredicate(m -> m.title().toLowerCase().contains(q));
        });

        // Подстановка данных при выборе строки в таблице
        tableMovies.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtTitle.setText(newSel.title());
                txtRating.setText(String.valueOf(newSel.rating()));

                // Разделяем жанр по символу "/" обратно на 2 выпадайки
                String[] parts = newSel.genre().split("/");
                cmbGenre1.setValue(parts[0].trim());
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    cmbGenre2.setValue(parts[1].trim());
                } else {
                    cmbGenre2.setValue(NO_SECOND_GENRE);
                }
            }
        });

        try {
            repository.initializeDatabase();
            refresh();
        } catch (SQLException e) {
            showError("Не удалось открыть базу данных");
        }
    }

    private void refresh() throws SQLException {
        items.setAll(repository.findAll());
        lblStatus.setText("Записей: " + items.size());
    }

    private String buildGenreString() {
        String g1 = cmbGenre1.getValue();
        String g2 = cmbGenre2.getValue();

        if (g1 == null || g1.isBlank()) {
            throw new IllegalArgumentException("Выберите основной жанр фильма");
        }
        if (g2 == null || g2.equals(NO_SECOND_GENRE) || g2.isBlank()) {
            return g1.trim();
        }
        if (g1.trim().equalsIgnoreCase(g2.trim())) {
            throw new IllegalArgumentException("Первый и второй жанры не должны совпадать");
        }
        return g1.trim() + " / " + g2.trim();
    }

    private double validateAndReadRating() {
        String title = txtTitle.getText().trim();
        if (title.isBlank()) {
            throw new IllegalArgumentException("Заполните название фильма");
        }
        double rating;
        try {
            rating = Double.parseDouble(txtRating.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Рейтинг должен быть числом");
        }
        if (!Double.isFinite(rating) || rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Рейтинг должен быть от 0 до 10");
        }
        return rating;
    }

    @FXML
    private void onAdd() {
        try {
            String combinedGenre = buildGenreString();
            double rating = validateAndReadRating();
            repository.insert(txtTitle.getText().trim(), combinedGenre, rating);
            clearFields();
            refresh();
        } catch (IllegalArgumentException | SQLException e) {
            showError(e instanceof SQLException ? "Ошибка сохранения в БД" : e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        Movie selected = tableMovies.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Выберите фильм в таблице для изменения");
            return;
        }
        try {
            String combinedGenre = buildGenreString();
            double rating = validateAndReadRating();
            repository.update(selected.id(), txtTitle.getText().trim(), combinedGenre, rating);
            clearFields();
            refresh();
        } catch (IllegalArgumentException | SQLException e) {
            showError(e instanceof SQLException ? "Ошибка обновления в БД" : e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        Movie selected = tableMovies.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Выберите фильм в таблице для удаления");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение удаления");
        confirm.setHeaderText("Удалить фильм «" + selected.title() + "»?");
        confirm.setContentText("Это действие нельзя отменить.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                repository.delete(selected.id());
                clearFields();
                refresh();
            } catch (SQLException e) {
                showError("Ошибка при удалении из БД");
            }
        }
    }

    @FXML
    private void onRefresh() {
        try {
            txtSearch.clear();
            clearFields();
            refresh();
        } catch (SQLException e) {
            showError("Ошибка обновления данных");
        }
    }

    private void clearFields() {
        txtTitle.clear();
        cmbGenre1.setValue(null);
        cmbGenre2.setValue(NO_SECOND_GENRE);
        txtRating.clear();
        tableMovies.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
