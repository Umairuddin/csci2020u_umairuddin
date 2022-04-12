module co.lab09 {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.csv;


    opens co.lab09 to javafx.fxml;
    exports co.lab09;
}