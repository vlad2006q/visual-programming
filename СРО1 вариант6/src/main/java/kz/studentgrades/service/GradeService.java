package kz.studentgrades.service;
import kz.studentgrades.model.StudentGrade;
import java.io.*; import java.nio.charset.StandardCharsets; import java.nio.file.*; import java.util.*;
public class GradeService {
    private final Path file = Paths.get("data", "grades.csv");
    public List<StudentGrade> load() {
        if (!Files.exists(file)) return demoData();
        List<StudentGrade> rows = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line; boolean first = true;
            while ((line = r.readLine()) != null) {
                if (first) { first = false; if (line.startsWith("studentName,")) continue; }
                String[] p = line.split(",", -1);
                if (p.length == 4) try { rows.add(new StudentGrade(p[0], p[1], p[2], Integer.parseInt(p[3]))); } catch (NumberFormatException ignored) { }
            }
        } catch (IOException e) { throw new RuntimeException("Ошибка чтения CSV: " + e.getMessage(), e); }
        return rows;
    }
    public void save(List<StudentGrade> rows) {
        try {
            Files.createDirectories(file.getParent()); Path tmp = file.resolveSibling("grades.tmp");
            try (BufferedWriter w = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
                w.write("studentName,groupName,subject,score"); w.newLine();
                for (StudentGrade g : rows) { w.write(clean(g.getStudentName()) + "," + clean(g.getGroupName()) + "," + clean(g.getSubject()) + "," + g.getScore()); w.newLine(); }
            }
            try { Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException e) { Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING); }
        } catch (IOException e) { throw new RuntimeException("Ошибка сохранения CSV: " + e.getMessage(), e); }
    }
    private String clean(String s) { return s.replace(",", " ").replace("\n", " ").trim(); }
    public List<StudentGrade> demoData() { return new ArrayList<>(List.of(
        new StudentGrade("Алиев Нурлан","ИС-24-1","Java",92), new StudentGrade("Серикова Алия","ИС-24-1","Базы данных",87),
        new StudentGrade("Иванов Артём","ИС-24-2","Java",78), new StudentGrade("Ким Диана","ИС-24-2","Математика",95),
        new StudentGrade("Ахметов Данияр","ИС-24-1","Базы данных",64), new StudentGrade("Петрова Мария","ИС-24-3","Java",88),
        new StudentGrade("Омарова Аружан","ИС-24-3","Математика",73), new StudentGrade("Садыков Тимур","ИС-24-2","Базы данных",49),
        new StudentGrade("Жумабекова Аяулым","ИС-24-1","Java",100), new StudentGrade("Нуртаев Али","ИС-24-3","Математика",81),
        new StudentGrade("Васильева Полина","ИС-24-2","Java",67), new StudentGrade("Ермеков Руслан","ИС-24-1","Математика",90)));
    }
}
