package com.esprit.controller;
import com.esprit.models.profile;
import com.esprit.models.user;

public class AccueilController {
    private user connectedUser;
    private profile userProfile;

    public void initData(user user, profile profile) {
        this.connectedUser = user;
        this.userProfile = profile;
        updateUI();
    }

    private void updateUI() {
        // Mettre à jour l'interface avec les données de l'utilisateur
    }
} 