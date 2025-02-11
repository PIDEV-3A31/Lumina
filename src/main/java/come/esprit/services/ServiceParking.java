package come.esprit.services;

import come.esprit.models.Parking;
import come.esprit.utils.DataBase;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ServiceParking implements CrudService<Parking> {


    private Connection connection;

    public ServiceParking() {
        connection = DataBase.getInstance().getConnection();
    }


    @Override
    public void ajouter(Parking parking) {
        String req = "INSERT INTO `parking`(`name_parck`, `capacity`, `status_parking`, `adresses`, `tarif`, `places_reservees`) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(req)) {
            pstmt.setString(1, parking.getName_parck());
            pstmt.setInt(2, parking.getCapacity());  // capacity en String (VARCHAR)
            pstmt.setString(3, parking.getStatus_parking());
            pstmt.setString(4, parking.getAdresses());
            pstmt.setString(5, parking.getTarif());  // tarif en String (VARCHAR)
            pstmt.setInt(6, parking.getPlaces_reservees());  // Ajout de place_reservees

            pstmt.executeUpdate();
            System.out.println("Parking ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du parking : " + e.getMessage());
        }
    }


    @Override
    public void modifier(Parking parking) {
        String req = "UPDATE `parking` SET `name_parck` = ?, `capacity` = ?, `status_parking` = ?, `adresses` = ?, `tarif` = ?, `places_reservees` = ? WHERE `id_parck` = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(req)) {
            pstmt.setString(1, parking.getName_parck());
            pstmt.setInt(2, parking.getCapacity());  // Si VARCHAR
            pstmt.setString(3, parking.getStatus_parking());
            pstmt.setString(4, parking.getAdresses());
            pstmt.setString(5, parking.getTarif());  // Si VARCHAR
            pstmt.setInt(6, parking.getPlaces_reservees());  // Mise à jour de place_reservees
            pstmt.setInt(7, parking.getId_parck());  // ID du parking à modifier

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Parking modifié avec succès !");
            } else {
                System.out.println("Aucun parking trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du parking : " + e.getMessage());
        }
    }


    @Override
    public List<Parking> afficher() {
        List<Parking> parkings = new ArrayList<>();
        String req = "SELECT * FROM `parking`";

        try (PreparedStatement pstmt = connection.prepareStatement(req);
             ResultSet rs = pstmt.executeQuery()) {

            // Boucle pour parcourir tous les résultats
            while (rs.next()) {
                int id_parck = rs.getInt("id_parck");
                String name_parck = rs.getString("name_parck");
                int capacity = rs.getInt("capacity");
                String status_parking = rs.getString("status_parking");
                String adresses = rs.getString("adresses");
                String tarif = rs.getString("tarif");
                int place_reservees = rs.getInt("places_reservees");  // Récupérer place_reservees

                // Créer un objet Parking et l'ajouter à la liste
                Parking parking = new Parking(name_parck, capacity, status_parking, adresses, tarif, place_reservees);
                parking.setId_parck(id_parck);  // Définir l'ID après l'avoir récupéré
                parkings.add(parking);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des parkings : " + e.getMessage());
        }

        return parkings;
    }




    @Override
    public void supprimer(int id_parck) {
        String req = "DELETE FROM `parking` WHERE `id_parck` = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(req)) {
            pstmt.setInt(1, id_parck);  // ID du parking à supprimer

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Parking supprimé avec succès !");
            } else {
                System.out.println("Aucun parking trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du parking : " + e.getMessage());
        }
    }


    //@Override
    //public List<Parking> getParkingsDisponibles() {
    //   return List.of();
    //}


}

