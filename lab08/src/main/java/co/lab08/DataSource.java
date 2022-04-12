package co.lab08;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataSource {
    public static ObservableList<StudentRecord> getAllMarks() {
        ObservableList<StudentRecord> marks =
                FXCollections.observableArrayList();
        marks.add(new StudentRecord("100100100", 80.0f, 64.0f, 74.25f));
        marks.add(new StudentRecord("100100101", 74.5f, 63.25f, 81.5f));
        marks.add(new StudentRecord("100100102", 92.0f, 95.0f, 92.5f));
        marks.add(new StudentRecord("100100103", 95.0f, 78.5f, 88.75f));
        marks.add(new StudentRecord("100100104", 58.25f, 78.5f, 59.55f));
        marks.add(new StudentRecord("100100105", 53.0f, 66.0f, 65.0f));
        marks.add(new StudentRecord("100100106", 67.5f, 66.5f, 69.5f));
        marks.add(new StudentRecord("100100107", 85.0f, 47.0f, 49.5f));
        marks.add(new StudentRecord("100100108", 43.0f, 32.5f, 53.75f));
        marks.add(new StudentRecord("100100109", 82.5f, 77.0f, 79.25f));

        return marks;
    }
}
