package com.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UserDetails {

    @FXML
    private TextField p_txt;

    @FXML
    private TextField u_txt;

    public TextField getP_txt() {
        return p_txt;
    }

    public void setP_txt(String p_txt) {
        this.p_txt.setText(p_txt);
    }

    public TextField getU_txt() {
        return u_txt;
    }

    public void setU_txt(String u_txt) {
        this.u_txt.setText(u_txt);
    }
}