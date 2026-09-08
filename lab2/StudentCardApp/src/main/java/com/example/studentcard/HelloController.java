package com.example.studentcard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class HelloController {

    @FXML private TextField txtName;
    @FXML private TextField txtSurname;
    @FXML private TextField txtAge;
    @FXML private TextField txtSpeciality;
    @FXML private ComboBox<String> cmbCourse;
    @FXML private ComboBox<String> cmbCity;
    @FXML private RadioButton rbFullTime;
    @FXML private RadioButton rbDistance;
    @FXML private CheckBox chkDorm;
    @FXML private Label lblResult;

    private final ToggleGroup studyFormGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        cmbCourse.getItems().addAll("1 курс", "2 курс", "3 курс", "4 курс");
        cmbCourse.getSelectionModel().selectFirst();

        cmbCity.getItems().addAll("Алматы", "Астана", "Шымкент", "Караганда", "Актобе");
        cmbCity.getSelectionModel().selectFirst();

        rbFullTime.setToggleGroup(studyFormGroup);
        rbDistance.setToggleGroup(studyFormGroup);
        rbFullTime.setSelected(true);
    }

    @FXML
    private void onCreateClick() {
        String name = txtName.getText().trim();
        String surname = txtSurname.getText().trim();
        String age = txtAge.getText().trim();
        String speciality = txtSpeciality.getText().trim();
        String course = cmbCourse.getValue();
        String city = cmbCity.getValue();

        if (name.isBlank() || surname.isBlank() || age.isBlank() || speciality.isBlank()) {
            showError("Заполните все поля.");
            return;
        }

        int studentAge;
        try {
            studentAge = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            showError("Возраст должен быть числом.");
            return;
        }

        if (studentAge < 16 || studentAge > 100) {
            showError("Введите корректный возраст.");
            return;
        }

        if (studyFormGroup.getSelectedToggle() == null) {
            showError("Выберите форму обучения.");
            return;
        }

        String studyForm = rbFullTime.isSelected() ? "Очная" : "Дистанционная";
        String dorm = chkDorm.isSelected() ? "Да" : "Нет";

        lblResult.setText(
                "Студент: " + surname + " " + name +
                "\nВозраст: " + studentAge +
                "\nСпециальность: " + speciality +
                "\nКурс: " + course +
                "\nГород: " + city +
                "\nФорма обучения: " + studyForm +
                "\nПроживает в общежитии: " + dorm
        );
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onClearClick() {
        txtName.clear();
        txtSurname.clear();
        txtAge.clear();
        txtSpeciality.clear();
        cmbCourse.getSelectionModel().selectFirst();
        cmbCity.getSelectionModel().selectFirst();
        rbFullTime.setSelected(true);
        chkDorm.setSelected(false);
        lblResult.setText("");
        txtName.requestFocus();
    }

    @FXML
    private void onExitClick() {
        Platform.exit();
    }
}
