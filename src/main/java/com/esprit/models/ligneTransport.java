package com.esprit.models;

import java.sql.Time;

public class ligneTransport {
    private int idLigne;
    private String nomLigne;
    private String zoneCouverture;
    private double tarif;
    private Time horaireDepart;
    private Time horaireArrivee;
    private String etat;
    private String lieuxDepart;
    private String lieuxArrive;

    // Constructeur sans ID
    public ligneTransport(String nomLigne, String zoneCouverture, double tarif, Time horaireDepart, Time horaireArrivee, String etat, String lieuxDepart, String lieuxArrive) {
        this.nomLigne = nomLigne;
        this.zoneCouverture = zoneCouverture;
        this.tarif = tarif;
        this.horaireDepart = horaireDepart;
        this.horaireArrivee = horaireArrivee;
        this.etat = etat;
        this.lieuxDepart = lieuxDepart;
        this.lieuxArrive = lieuxArrive;
    }

    // Constructeur avec ID
    public ligneTransport(int idLigne, String nomLigne, String zoneCouverture, double tarif, Time horaireDepart, Time horaireArrivee, String etat, String lieuxDepart, String lieuxArrive) {
        this.idLigne = idLigne;
        this.nomLigne = nomLigne;
        this.zoneCouverture = zoneCouverture;
        this.tarif = tarif;
        this.horaireDepart = horaireDepart;
        this.horaireArrivee = horaireArrivee;
        this.etat = etat;
        this.lieuxDepart = lieuxDepart;
        this.lieuxArrive = lieuxArrive;
    }

    // Getters et Setters
    public int getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(int idLigne) {
        this.idLigne = idLigne;
    }

    public String getNomLigne() {
        return nomLigne;
    }

    public void setNomLigne(String nomLigne) {
        this.nomLigne = nomLigne;
    }

    public String getZoneCouverture() {
        return zoneCouverture;
    }

    public void setZoneCouverture(String zoneCouverture) {
        this.zoneCouverture = zoneCouverture;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public Time getHoraireDepart() {
        return horaireDepart;
    }

    public void setHoraireDepart(Time horaireDepart) {
        this.horaireDepart = horaireDepart;
    }

    public Time getHoraireArrivee() {
        return horaireArrivee;
    }

    public void setHoraireArrivee(Time horaireArrivee) {
        this.horaireArrivee = horaireArrivee;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getLieuxDepart() {
        return lieuxDepart;
    }

    public void setLieuxDepart(String lieuxDepart) {
        this.lieuxDepart = lieuxDepart;
    }

    public String getLieuxArrive() {
        return lieuxArrive;
    }

    public void setLieuxArrive(String lieuxArrive) {
        this.lieuxArrive = lieuxArrive;
    }

    @Override
    public String toString() {
        return "ligneTransport{" +
                "idLigne=" + idLigne +
                ", nomLigne='" + nomLigne + '\'' +
                ", zoneCouverture='" + zoneCouverture + '\'' +
                ", tarif=" + tarif +
                ", horaireDepart=" + horaireDepart +
                ", horaireArrivee=" + horaireArrivee +
                ", etat='" + etat + '\'' +
                ", lieuxDepart='" + lieuxDepart + '\'' +
                ", lieuxArrive='" + lieuxArrive + '\'' +
                '}';
    }
}