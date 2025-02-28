package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class StripePayement {
    @FXML
    private WebView webView;

    private static double montant;  // Stocker le montant ici

    public static void setMontant(double prix) {
        montant = prix;
    }

    private boolean paymentSuccessful = false; // Indicateur de succès du paiement

    public void setPaymentSuccessful(boolean status) {
        this.paymentSuccessful = status;
    }

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        String url = "http://127.0.0.1:8000/?montant=" + URLEncoder.encode(String.valueOf(montant), StandardCharsets.UTF_8);
        webEngine.load(url);

        // Injecter l'objet Java pour permettre la communication avec JavaScript
        webEngine.documentProperty().addListener((observable, oldDoc, newDoc) -> {
            if (newDoc != null) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaBridge", this);
            }
        });
    }

    // Cette méthode sera appelée depuis JavaScript
    public void paymentStatus(String status) {
        System.out.println("Statut du paiement reçu depuis JavaScript : " + status);

        if ("SUCCESS".equals(status)) {
            paymentSuccessful = true;

        } else {
            paymentSuccessful = false;
        }
    }

}
