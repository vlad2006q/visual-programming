module kz.studentgrades {
    requires javafx.controls;
    requires javafx.fxml;
    exports kz.studentgrades;
    exports kz.studentgrades.model;
    opens kz.studentgrades.controller to javafx.fxml;
}
