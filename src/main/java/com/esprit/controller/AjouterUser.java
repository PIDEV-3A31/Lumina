package com.esprit.controller;
import com.esprit.models.user;


import com.esprit.services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AjouterUser {
    @FXML
    private TextField password_textfield;

    @FXML
    private TextField username_textfield;

    public TextField getPassword_textfield() {
        return password_textfield;
    }

    public void setPassword_textfield(String password_textfield) {
        this.password_textfield.setText(password_textfield);
    }

    public TextField getUsername_textfield() {
        return username_textfield;
    }

    public void setUsername_textfield(String username_textfield) {
        this.username_textfield.setText(username_textfield);
    }

    @FXML
    void ajouter_user(ActionEvent event) {
        String password = password_textfield.getText();
        String username = username_textfield.getText();
        user u = new user(username,password);
        ServiceUser su = new ServiceUser();
        su.ajouter(u);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("succes");
        alert.setHeaderText("user ajouter avec succes");
        alert.showAndWait();
        ///appel du interface pour la redirection
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDetails.fxml"));
        try {
            Parent root = loader.load();
            UserDetails userDetails = loader.getController();
            userDetails.setU_txt(username);
            userDetails.setP_txt(password);
            username_textfield.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
