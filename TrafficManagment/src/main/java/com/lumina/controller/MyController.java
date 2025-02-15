package com.lumina.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MyController {

    @FXML
    private Button button;  // Define the button field and annotate it with @FXML

    @FXML
    private TextField textField;  // Define the TextField field and annotate it with @FXML

    // The initialize method will be called after FXML is loaded
    @FXML
    public void initialize() {
        // Set an action for the button
        button.setOnAction(e -> {
            textField.setText("Vous avez cliqué");
        });
    }
}
