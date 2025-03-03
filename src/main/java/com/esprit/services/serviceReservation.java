package com.esprit.services;

import com.esprit.models.reservation;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class serviceReservation implements CrudTransport<reservation>{

    private Connection connection;

    public serviceReservation() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(reservation reservation) {
        if (!verifierDisponibilite(reservation.getIdTransport(), reservation.getNbPlaces())) {
            System.out.println("Réservation impossible : Plus de places disponibles !");
            return;
        }

        String req = "INSERT INTO reservation (id_utilisateur, id_moyenTransport, date_reservation, nb_places, tarif_total, statut) VALUES (?, ?, ?, ?, ?, ?)";
        String updatePlacesReservees = "UPDATE moyen_transport SET places_reservees = places_reservees + ? WHERE id_moyenTransport = ?";

        try {
            connection.setAutoCommit(false);


            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reservation.getIdUtilisateur());
            ps.setInt(2, reservation.getIdTransport());
            ps.setTimestamp(3, new Timestamp(reservation.getDateReservation().getTime()));
            ps.setInt(4, reservation.getNbPlaces());
            ps.setDouble(5, reservation.getTarifTotal());
            ps.setString(6, reservation.getStatut());
            ps.executeUpdate();


            PreparedStatement psUpdate = connection.prepareStatement(updatePlacesReservees);
            psUpdate.setInt(1, reservation.getNbPlaces());
            psUpdate.setInt(2, reservation.getIdTransport());
            psUpdate.executeUpdate();


            connection.commit();
            System.out.println("Réservation ajoutée avec succès et places réservées mises à jour !");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Erreur lors du rollback : " + rollbackEx.getMessage());
            }
            System.out.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la réactivation du commit automatique : " + ex.getMessage());
            }
        }
    }


    @Override
    public void modifier(reservation reservation) {
        String req = "UPDATE reservation SET id_utilisateur=?, id_moyenTransport=?, date_reservation=?, nb_places=?, tarif_total=?, statut=? WHERE id_reservation=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reservation.getIdUtilisateur());
            ps.setInt(2, reservation.getIdTransport());
            ps.setTimestamp(3, new Timestamp(reservation.getDateReservation().getTime()));
            ps.setInt(4, reservation.getNbPlaces());
            ps.setDouble(5, reservation.getTarifTotal());
            ps.setString(6, reservation.getStatut());
            ps.setInt(7, reservation.getIdReservation());
            ps.executeUpdate();
            System.out.println(" Réservation modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println(" Erreur lors de la modification de la réservation : " + e.getMessage());
        }
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM reservation WHERE id_reservation=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println(" Réservation supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println(" Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }


    @Override
    public List<reservation> consulter() {
        List<reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                reservation reservation = new reservation(
                        rs.getInt("id_reservation"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_moyenTransport"),
                        rs.getTimestamp("date_reservation"),
                        rs.getInt("nb_places"),
                        rs.getDouble("tarif_total"),
                        rs.getString("statut")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println(" Erreur lors de la consultation des réservations : " + e.getMessage());
        }
        return reservations;
    }

    public boolean verifierDisponibilite(int idTransport, int nbPlaces) {
        String sql = "SELECT capacite_max, places_reservees FROM moyen_transport WHERE id_moyenTransport = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idTransport);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int capaciteMax = rs.getInt("capacite_max");
                int placesReservees = rs.getInt("places_reservees");
                int placesDisponibles = capaciteMax - placesReservees;

                return nbPlaces <= placesDisponibles;
            }
        } catch (SQLException e) {
            System.out.println(" Erreur lors de la verification de la disponiblite de place pour la réservation : " + e.getMessage());
        }
        return false;
    }

    public String getTypeMoyenTransport(int idMoyenTransport) {
        String type = "";
        String query = "SELECT type FROM moyen_transport WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idMoyenTransport);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    type = rs.getString("type");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Afficher l'erreur en console
        }

        return type;
    }



}

