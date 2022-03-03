module com.example.lab04 {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.validator;
    requires java.desktop;


    opens com.example.lab04 to javafx.fxml;
    exports com.example.lab04;
}