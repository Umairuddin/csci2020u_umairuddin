package co.lab09;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Lab09Controller {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}