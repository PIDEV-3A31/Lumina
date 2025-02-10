package com.esprit.models;

import java.util.Date;

public class Demandes {
    private int id_demande;
    private int id_utilisateur;
    private int id_document;
    private Date date_demande;
    private String statut_demande;

    public Demandes() {}

    // Constructeur paramétré
    public Demandes(int id_utilisateur, int id_document, Date date_demande, String statut_demande) {
        this.id_utilisateur = id_utilisateur;
        this.id_document = id_document;
        this.date_demande = date_demande;
        this.statut_demande = statut_demande;
    }

    public Demandes(int id_demande,int id_utilisateur, int id_document, Date date_demande, String statut_demande) {
        this.id_demande = id_demande;
        this.id_utilisateur = id_utilisateur;
        this.id_document = id_document;
        this.date_demande = date_demande;
        this.statut_demande = statut_demande;
    }

    public int getId_demande() {
        return id_demande;
    }

    public void setId_demande(int id_demande) {
        this.id_demande = id_demande;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public int getId_document() {
        return id_document;
    }

    public void setId_document(int id_document) {
        this.id_document = id_document;
    }

    public Date getDate_demande() {
        return date_demande;
    }

    public void setDate_demande(Date date_demande) {
        this.date_demande = date_demande;
    }

    public String getStatut_demande() {
        return statut_demande;
    }

    public void setStatut_demande(String statut_demande) {
        this.statut_demande = statut_demande;
    }
}
