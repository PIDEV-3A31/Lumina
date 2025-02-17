package come.esprit.models;

import java.sql.Timestamp;

public class Reservation {
    private int id_reservation;
    private int id_parck;
    private Timestamp date_reservation;
    private String matricule_voiture;

    public Reservation() {

    }


    public int getId_reservation() {
        return id_reservation;
    }

    public void setId_reservation(int id_reservation) {
        this.id_reservation = id_reservation;
    }

    public int getId_parck() {
        return id_parck;
    }

    public void setId_parck(int id_parck) {
        this.id_parck = id_parck;
    }

    public Timestamp getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(Timestamp date_reservation) {
        this.date_reservation = date_reservation;
    }

    public String getMatricule_voiture() {
        return matricule_voiture;
    }

    public void setMatricule_voiture(String matricule_voiture) {
        this.matricule_voiture = matricule_voiture;
    }

    public Reservation(int idParck, Timestamp dateReservation, String matriculeVoiture, int id_reservation) {
        this.id_parck = idParck;
        this.date_reservation = dateReservation;
        this.matricule_voiture = matriculeVoiture;
        this.id_reservation = id_reservation;

    }

    public Reservation(int id_parck, Timestamp date_reservation, String matricule_voiture) {
        this.id_parck = id_parck;
        this.date_reservation = date_reservation;
        this.matricule_voiture = matricule_voiture;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id_reservation=" + id_reservation +
                ", id_parck=" + id_parck +
                ", date_reservation='" + date_reservation + '\'' +
                ", matricule_voiture='" + matricule_voiture + '\'' +
                '}';
    }
}