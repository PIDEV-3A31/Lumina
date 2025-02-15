package com.esprit.models;

public class moyenTransport {
    private int idMoyenTransport;
    private int idLigne;
    private String typeTransport;
    private int capaciteMax;
    private String immatriculation;
    private String etat;
    private int place_reservees;


    public moyenTransport() {
    }


    public moyenTransport(int idMoyenTransport, int idLigne, String typeTransport, int capaciteMax, String immatriculation, String etat) {
        this.idMoyenTransport = idMoyenTransport;
        this.idLigne = idLigne;
        this.typeTransport = typeTransport;
        this.capaciteMax = capaciteMax;
        this.immatriculation = immatriculation;
        this.etat = etat;
        this.place_reservees = 0;
    }

    public moyenTransport(int idLigne, String typeTransport, int capaciteMax, String immatriculation, String etat) {
        this.idLigne = idLigne;
        this.typeTransport = typeTransport;
        this.capaciteMax = capaciteMax;
        this.immatriculation = immatriculation;
        this.etat = etat;
        this.place_reservees = 0;
    }


    public int getIdTransport() {
        return idMoyenTransport;
    }

    public void setIdTransport(int idMoyenTransport) {
        this.idMoyenTransport = idMoyenTransport;
    }

    public int getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(int idLigne) {
        this.idLigne = idLigne;
    }

    public String getTypeTransport() {
        return typeTransport;
    }

    public void setTypeTransport(String typeTransport) {
        this.typeTransport = typeTransport;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }


    @Override
    public String toString() {
        return "MoyenTransport{" +
                "idMoyenTransport=" + idMoyenTransport +
                ", idLigne=" + idLigne +
                ", typeTransport='" + typeTransport + '\'' +
                ", capaciteMax=" + capaciteMax +
                ", immatriculation='" + immatriculation + '\'' +
                ", etat='" + etat + '\'' +
                '}';
    }

    public int getPlace_reservees() {
        return place_reservees;
    }

    public void setPlace_reservees(int place_reservees) {
        this.place_reservees = place_reservees;
    }
}