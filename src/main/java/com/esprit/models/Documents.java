package com.esprit.models;

import java.util.Date;

public class Documents {
    private int id_document;

    private String type_document;
    private String titre;
    private String description;
    private Date date_creation;
    private Date date_modification;
    private String chemin_fichier;

    // Constructeurs
    public Documents() {}

    public Documents(String type_document, String titre, String description, Date date_creation, Date date_modification, String chemin_fichier) {
        this.type_document = type_document;
        this.titre = titre;
        this.description = description;
        this.date_creation = date_creation;
        this.date_modification = date_modification;
        this.chemin_fichier = chemin_fichier;
    }

    public Documents(int id_document,String type_document, String titre, String description, Date date_creation, Date date_modification, String chemin_fichier) {
        this.id_document = id_document;
        this.type_document = type_document;
        this.titre = titre;
        this.description = description;
        this.date_creation = date_creation;
        this.date_modification = date_modification;
        this.chemin_fichier = chemin_fichier;
    }

    public int getId_document() {
        return id_document;
    }

    public void setId_document(int id_document) {
        this.id_document = id_document;
    }

    public String getType_document() {
        return type_document;
    }

    public void setType_document(String type_document) {
        this.type_document = type_document;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Date date_creation) {
        this.date_creation = date_creation;
    }

    public Date getDate_modification() {
        return date_modification;
    }

    public void setDate_modification(Date date_modification) {
        this.date_modification = date_modification;
    }

    public String getChemin_fichier() {
        return chemin_fichier;
    }

    public void setChemin_fichier(String chemin_fichier) {
        this.chemin_fichier = chemin_fichier;
    }
}
