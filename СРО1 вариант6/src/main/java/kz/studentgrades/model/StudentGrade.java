package kz.studentgrades.model;
import javafx.beans.property.*;
public class StudentGrade {
    private final StringProperty studentName = new SimpleStringProperty();
    private final StringProperty groupName = new SimpleStringProperty();
    private final StringProperty subject = new SimpleStringProperty();
    private final IntegerProperty score = new SimpleIntegerProperty();
    public StudentGrade(String studentName, String groupName, String subject, int score) {
        setStudentName(studentName); setGroupName(groupName); setSubject(subject); setScore(score);
    }
    public String getStudentName(){return studentName.get();} public void setStudentName(String v){studentName.set(v);} public StringProperty studentNameProperty(){return studentName;}
    public String getGroupName(){return groupName.get();} public void setGroupName(String v){groupName.set(v);} public StringProperty groupNameProperty(){return groupName;}
    public String getSubject(){return subject.get();} public void setSubject(String v){subject.set(v);} public StringProperty subjectProperty(){return subject;}
    public int getScore(){return score.get();} public void setScore(int v){score.set(v);} public IntegerProperty scoreProperty(){return score;}
    public String getResult(){if(getScore()>=90)return "Отлично";if(getScore()>=75)return "Хорошо";if(getScore()>=50)return "Удовлетворительно";return "Неудовлетворительно";}
}
