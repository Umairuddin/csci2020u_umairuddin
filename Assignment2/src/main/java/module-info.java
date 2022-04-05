module com.a2.assignment2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.xml;
    requires commons.csv;

    opens com.a2.assignment2 to javafx.fxml;
    exports com.a2.assignment2;
}