package come.esprit.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import come.esprit.models.Parking;
import come.esprit.models.Reservation;
import come.esprit.utils.DataBase;

import java.io.File;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class ServiceReservation implements CrudService<Reservation> {

    private final Connection connection;

    public ServiceReservation() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Reservation reservation) {
        // Vérifier la disponibilité des places dans le parking
        String checkAvailabilityQuery = "SELECT capacity, places_reservees FROM parking WHERE id_parck = ?";

        try (PreparedStatement pstmtCheck = connection.prepareStatement(checkAvailabilityQuery)) {
            pstmtCheck.setInt(1, reservation.getId_parck());
            ResultSet rs = pstmtCheck.executeQuery();

            if (rs.next()) {
                int capacity = rs.getInt("capacity");
                int placesReservees = rs.getInt("places_reservees");

                // Vérifier si le nombre de places disponibles est suffisant pour la réservation
                if ((capacity - placesReservees) > 0) {
                    // Si suffisamment de places sont disponibles, ajouter la réservation
                    String req = "INSERT INTO reservation (id_parck, matricule_voiture) VALUES (?, ?)";

                    try (PreparedStatement pstmtInsert = connection.prepareStatement(req)) {
                        pstmtInsert.setInt(1, reservation.getId_parck());

                        pstmtInsert.setString(2, reservation.getMatricule_voiture());

                        pstmtInsert.executeUpdate();
                        System.out.println("Réservation ajoutée avec succès !");

                        // Mettre à jour le nombre de places réservées dans le parking (incrémenter de 1)
                        String updateParkingQuery = "UPDATE parking SET places_reservees = places_reservees + 1 WHERE id_parck = ?";
                        try (PreparedStatement pstmtUpdate = connection.prepareStatement(updateParkingQuery)) {
                            pstmtUpdate.setInt(1, reservation.getId_parck());
                            pstmtUpdate.executeUpdate();
                        }

                    } catch (SQLException e) {
                        System.out.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
                    }
                } else {
                    System.out.println("Réservation impossible : Il n'y a pas assez de places disponibles !");
                }
            } else {
                System.out.println("Parking non trouvé !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de la disponibilité : " + e.getMessage());
        }
    }



    @Override
    public void modifier(Reservation reservation) {
        String req = "UPDATE Reservation SET id_parck = ?, matricule_voiture = ? WHERE id_reservation = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, reservation.getId_parck());

            ps.setString(2, reservation.getMatricule_voiture());
            ps.setInt(3, reservation.getId_reservation());
            ps.executeUpdate();
            System.out.println("Réservation modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la réservation : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id_reservation) {
        String req = "DELETE FROM Reservation WHERE id_reservation = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id_reservation);
            ps.executeUpdate();
            System.out.println("Réservation supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }

    @Override




    public List<Reservation> afficher() {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM Reservation";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Reservation reservation = new Reservation(

                        rs.getInt("id_parck"),
                        rs.getTimestamp("date_reservation"),
                        rs.getString("matricule_voiture"),
                        rs.getInt("id_reservation")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation des réservations : " + e.getMessage());
        }
        return reservations;
    }

    // Méthode pour vérifier si des places sont disponibles dans le parking
    private boolean verifierDisponibilite(int id_parck) {
        String req = "SELECT capacity - places_reservees AS places_disponibles FROM Parking WHERE id_parck = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id_parck);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("places_disponibles") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de la disponibilité : " + e.getMessage());
        }
        return false;
    }




    public class QRCodeGenerator {
        public static void generateQRCode(String data, String filePath) throws Exception {
            int width = 300;
            int height = 300;
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);
            Path path = new File(filePath).toPath();
            MatrixToImageWriter.writeToPath(matrix, "PNG", path);
        }
    }

}
