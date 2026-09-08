module com.example.studentcard {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.studentcard to javafx.fxml;
    exports com.example.studentcard;
}
