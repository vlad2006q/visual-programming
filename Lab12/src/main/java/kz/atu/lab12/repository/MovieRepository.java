package kz.atu.lab12.repository;

import kz.atu.lab12.model.Movie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private static final String URL = "jdbc:sqlite:movies.db";

    public void initializeDatabase() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS movies (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                genre TEXT NOT NULL,
                rating REAL NOT NULL CHECK (rating BETWEEN 0 AND 10)
            )
            """;
        try (Connection c = DriverManager.getConnection(URL);
             Statement st = c.createStatement()) {
            st.execute(sql);
        }
        seedInitialData();
    }

    private void seedInitialData() throws SQLException {
        String countSql = "SELECT COUNT(*) FROM movies";
        try (Connection c = DriverManager.getConnection(URL);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(countSql)) {
            if (rs.next() && rs.getInt(1) > 0) {
                return; // База уже заполнена, дублировать не нужно
            }
        }

        Object[][] initialMovies = {
                {"Побег из Шоушенка", "Драма", 9.3},
                {"Крёстный отец", "Криминал / Драма", 9.2},
                {"Тёмный рыцарь", "Боевик / Триллер", 9.0},
                {"Начало", "Фантастика / Боевик", 8.8},
                {"Интерстеллар", "Фантастика / Драма", 8.7},
                {"Бойцовский клуб", "Триллер / Драма", 8.8},
                {"Форрест Гамп", "Драма / Мелодрама", 8.8},
                {"Матрица", "Фантастика / Боевик", 8.7},
                {"Зелёная миля", "Драма / Фэнтези", 9.1},
                {"Список Шиндлера", "Биография / Драма", 9.0},
                {"Властелин колец: Возвращение короля", "Фэнтези / Приключения", 9.0},
                {"Паразиты", "Триллер / Драма", 8.5},
                {"Унесённые призраками", "Аниме / Фэнтези", 8.6},
                {"Оппенгеймер", "Биография / История", 8.4},
                {"Дюна: Часть вторая", "Фантастика / Приключения", 8.6}
        };

        String insertSql = "INSERT INTO movies(title, genre, rating) VALUES (?, ?, ?)";
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(insertSql)) {
            for (Object[] m : initialMovies) {
                ps.setString(1, (String) m[0]);
                ps.setString(2, (String) m[1]);
                ps.setDouble(3, (Double) m[2]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public List<Movie> findAll() throws SQLException {
        List<Movie> result = new ArrayList<>();
        String sql = "SELECT id, title, genre, rating FROM movies ORDER BY id";
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getDouble("rating")
                ));
            }
        }
        return result;
    }

    public void insert(String title, String genre, double rating) throws SQLException {
        String sql = "INSERT INTO movies(title, genre, rating) VALUES (?, ?, ?)";
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, genre);
            ps.setDouble(3, rating);
            ps.executeUpdate();
        }
    }

    public int update(int id, String title, String genre, double rating) throws SQLException {
        String sql = "UPDATE movies SET title=?, genre=?, rating=? WHERE id=?";
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, genre);
            ps.setDouble(3, rating);
            ps.setInt(4, id);
            return ps.executeUpdate();
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id=?";
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}
