package kz.studentgrades.controller;
import javafx.fxml.FXML; import javafx.scene.control.*; import javafx.stage.Stage; import kz.studentgrades.model.StudentGrade;
public class GradeFormController {
    @FXML private TextField studentField, groupField, subjectField, scoreField; @FXML private Label errorLabel;
    private StudentGrade grade; private boolean saved;
    public void setGrade(StudentGrade value) { grade=value; if(value!=null){studentField.setText(value.getStudentName());groupField.setText(value.getGroupName());subjectField.setText(value.getSubject());scoreField.setText(String.valueOf(value.getScore()));} }
    public StudentGrade getGrade(){return grade;} public boolean isSaved(){return saved;}
    @FXML private void save() {
        String n=studentField.getText().trim(), g=groupField.getText().trim(), s=subjectField.getText().trim(), p=scoreField.getText().trim();
        if(n.isEmpty()||g.isEmpty()||s.isEmpty()||p.isEmpty()){errorLabel.setText("Заполните все поля.");return;}
        int score; try{score=Integer.parseInt(p);}catch(NumberFormatException e){errorLabel.setText("Баллы должны быть целым числом.");return;}
        if(score<0||score>100){errorLabel.setText("Баллы должны быть от 0 до 100.");return;}
        if(grade==null)grade=new StudentGrade(n,g,s,score);else{grade.setStudentName(n);grade.setGroupName(g);grade.setSubject(s);grade.setScore(score);}
        saved=true; ((Stage)studentField.getScene().getWindow()).close();
    }
    @FXML private void cancel(){((Stage)studentField.getScene().getWindow()).close();}
}
