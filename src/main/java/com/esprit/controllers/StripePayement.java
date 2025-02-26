package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class StripePayement {

    @FXML
    private WebView webView;

    @FXML
    public void initialize() {

        WebEngine webEngine = webView.getEngine();
        webEngine.load("http:// 127.0.0.1:8000/");

    }
}
