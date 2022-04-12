module co.lab08 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires commons.csv;


    opens co.lab08 to javafx.fxml;
    exports co.lab08;
}