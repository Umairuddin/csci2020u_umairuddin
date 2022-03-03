package com.example.lab04;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.validator.routines.EmailValidator;


public class HelloController {
    @FXML private Label response;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private TextField fullname;
    @FXML private TextField email;
    @FXML private TextField phone;
    @FXML private DatePicker dob;

    private String phoneRestrictions = "(\\d{3})(\\d{3})(\\d+)";
    private String phoneOutput = "($1)-$2-$3";

            // On clicking register function
    @FXML protected void registerClick() {
        response.setText("");
        EmailValidator ev = EmailValidator.getInstance(false);

        if(!ev.isValid(email.getText())){
            response.setText("Invalid Email Address");
        } else {
            System.out.printf("Username: %s | Password: %s | Email: %s | Phone: %s | Date of Birth: %s",
                    username.getText(),
                    password.getText(),
                    email.getText(),
                    phone.getText(),
                    dob.getValue());
            response.setText("Successfully registered!");
        }
    }

    @FXML void phoneAction(javafx.event.ActionEvent actionEvent) {
        String s = phone.getText().replaceFirst(phoneRestrictions, phoneOutput);
        phone.setText(s);
    }
}
