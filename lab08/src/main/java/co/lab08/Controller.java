package co.lab08;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Controller {
    //FileMenu Options
    private String currentFilename = "studentRecord.csv";

    @FXML
    private TableView tableView;
    //Adding Button
    @FXML
    private TextField studentID;
    @FXML
    private TextField assignments;
    @FXML
    private TextField midterm;
    @FXML
    private TextField finalExam;
    @FXML
    public void initialize(){
        TableColumn<StudentRecord, String> StudentIDColumn = new TableColumn<>("Student ID");
        StudentIDColumn.setCellValueFactory(new PropertyValueFactory<>("StudentID"));

        TableColumn<StudentRecord, Float> MidtermColumn = new TableColumn<>("Midterm");
        MidtermColumn.setCellValueFactory(new PropertyValueFactory<>("Midterm"));

        TableColumn<StudentRecord, Float> AssignmentsColumn = new TableColumn<>("Assignments");
        AssignmentsColumn.setCellValueFactory(new PropertyValueFactory<>("Assignments"));

        TableColumn<StudentRecord, Float> FinalExamColumn = new TableColumn<>("Final Exam");
        FinalExamColumn.setCellValueFactory(new PropertyValueFactory<>("FinalExam"));

        TableColumn<StudentRecord, Float> FinalMarkColumn = new TableColumn<>("Final Mark");
        FinalMarkColumn.setCellValueFactory(new PropertyValueFactory<>("FinalMark"));

        TableColumn<StudentRecord, String> LetterGradeColumn = new TableColumn<>("Letter Grade");
        LetterGradeColumn.setCellValueFactory(new PropertyValueFactory<>("Grade"));

        tableView.getColumns().add(StudentIDColumn);
        tableView.getColumns().add(MidtermColumn);
        tableView.getColumns().add(AssignmentsColumn);
        tableView.getColumns().add(FinalExamColumn);
        tableView.getColumns().add(FinalMarkColumn);
        tableView.getColumns().add(LetterGradeColumn);

        tableView.getItems().addAll(DataSource.getAllMarks());

        tableView.setPlaceholder(new Label("No rows to display!"));

    }


    @FXML
    public void onAddClick(ActionEvent actionEvent) {
        float assgn = Float.parseFloat(assignments.getText());
        float mid = Float.parseFloat(midterm.getText());
        float fin = Float.parseFloat(finalExam.getText());
        StudentRecord record = new StudentRecord(studentID.getText(), mid, assgn, fin);

        tableView.getItems().add(record);
    }

    @FXML
    public void onNewClick(ActionEvent actionEvent) {
        tableView.getItems().clear();
        currentFilename = "";
    }

    @FXML
    public void onOpenClick(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Records File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Record Files", "*.csv"));
        File selectedFile = chooser.showOpenDialog(Lab08.stage);
        if(selectedFile != null)
            currentFilename = selectedFile.getName();
        load();
    }

    @FXML
    public void onSaveClick(ActionEvent actionEvent) {
        if(currentFilename == "")
            onSaveAsClick(actionEvent);
        else
            save();
    }

    @FXML
    public void onSaveAsClick(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Records File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Record Files", "*.csv"));
        File selectedFile = chooser.showSaveDialog(Lab08.stage);
        if(selectedFile != null)
            currentFilename = selectedFile.getName();
        save();
    }

    @FXML
    public void onExitClick(ActionEvent actionEvent) {
        Lab08.stage.close();
    }

    private void save(){
        ObservableList<StudentRecord> data = tableView.getItems();
        try{
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(currentFilename));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.Builder.create().build());
            for(StudentRecord record : data){
                csvPrinter.printRecord(record.getStudentID(), record.getMidterm(), record.getAssignments(), record.getFinalExam());
            }
            csvPrinter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void load(){
        ObservableList<StudentRecord> records = FXCollections.observableArrayList();
        try{
            Reader in = new FileReader(currentFilename);
            Iterable<CSVRecord> inputs = CSVFormat.Builder.create().setHeader("StudentID", "Midterm", "Assignments", "FinalExam").build().parse(in);

            for(CSVRecord rec : inputs){
                StudentRecord record = new StudentRecord(rec.get("StudentID"),
                        Float.parseFloat(rec.get("Midterm")),
                        Float.parseFloat(rec.get("Assignments")),
                        Float.parseFloat(rec.get("FinalExam")));
                records.add(record);
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        tableView.getItems().setAll(records);
    }
}