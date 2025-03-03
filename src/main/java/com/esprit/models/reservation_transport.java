package com.esprit.models;

import java.util.Date;

public class reservation_transport {
    private int idReservation;
    private int idUtilisateur;
    private int idMoyenTransport;
    private Date dateReservation;
    private int nbPlaces;
    private double tarifTotal;
    private String statut;

    public reservation_transport(int idReservation, int idUtilisateur, int idMoyenTransport, Date dateReservation, int nbPlaces, double tarifTotal, String statut) {
        this.idReservation = idReservation;
        this.idUtilisateur = idUtilisateur;
        this.idMoyenTransport = idMoyenTransport;
        this.dateReservation = dateReservation;
        this.nbPlaces = nbPlaces;
        this.tarifTotal = tarifTotal;
        this.statut = statut;
    }

    public reservation_transport(int idUtilisateur, int idMoyenTransport, Date dateReservation, int nbPlaces, double tarifTotal, String statut) {
        this.idUtilisateur = idUtilisateur;
        this.idMoyenTransport = idMoyenTransport;
        this.dateReservation = dateReservation;
        this.nbPlaces = nbPlaces;
        this.tarifTotal = tarifTotal;
        this.statut = statut;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdTransport() {
        return idMoyenTransport;
    }

    public void setIdTransport(int idMoyenTransport) {
        this.idMoyenTransport = idMoyenTransport;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public double getTarifTotal() {
        return tarifTotal;
    }

    public void setTarifTotal(double tarifTotal) {
        this.tarifTotal = tarifTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "reservation{" +
                "idReservation=" + idReservation +
                ", idUtilisateur=" + idUtilisateur +
                ", idMoyenTransport=" + idMoyenTransport +
                ", dateReservation=" + dateReservation +
                ", nbPlaces=" + nbPlaces +
                ", tarifTotal=" + tarifTotal +
                ", statut='" + statut + '\'' +
                '}';
    }
}
