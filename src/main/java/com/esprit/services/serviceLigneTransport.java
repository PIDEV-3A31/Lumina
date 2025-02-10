package com.esprit.services;
import com.esprit.models.ligneTransport;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class serviceLigneTransport implements CrudTransport<ligneTransport> {

    private Connection connection;

    public serviceLigneTransport() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(ligneTransport ligneTransport) {
        String req = "INSERT INTO ligne_transport (nom_ligne, zone_couverture, tarif, horaire_depart, horaire_arrivee, etat) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, ligneTransport.getNomLigne());
            ps.setString(2, ligneTransport.getZoneCouverture());
            ps.setDouble(3, ligneTransport.getTarif());
            ps.setTime(4, new java.sql.Time(ligneTransport.getHoraireDepart().getTime()));
            ps.setTime(5, new java.sql.Time(ligneTransport.getHoraireArrivee().getTime()));
            ps.setString(6, ligneTransport.getEtat());

            ps.executeUpdate();
            System.out.println("Ligne de transport ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(" Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void modifier(ligneTransport ligneTransport) {
        String req = "UPDATE ligne_transport SET nom_ligne = ?, zone_couverture = ?, tarif = ?, horaire_depart = ?, horaire_arrivee = ?, etat = ? " +
                "WHERE id_ligne = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            // Set the values for the SQL query
            ps.setString(1, ligneTransport.getNomLigne());
            ps.setString(2, ligneTransport.getZoneCouverture());
            ps.setDouble(3, ligneTransport.getTarif());
            ps.setTime(4, new java.sql.Time(ligneTransport.getHoraireDepart().getTime()));
            ps.setTime(5, new java.sql.Time(ligneTransport.getHoraireArrivee().getTime()));
            ps.setString(6, ligneTransport.getEtat());
            ps.setInt(7, ligneTransport.getIdLigne()); // Assuming `getIdLigne()` will give the ID for updating the record.

            // Execute the update
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Ligne de transport modifiée avec succès !");
            } else {
                System.out.println("Aucune ligne trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM ligne_transport WHERE id_ligne = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            // Set the id of the ligneTransport to be deleted
            ps.setInt(1, id);

            // Execute the delete operation
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Ligne de transport supprimée avec succès !");
            } else {
                System.out.println("Aucune ligne trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }


    public List<ligneTransport> consulter() {
        List<ligneTransport> lignesTransport = new ArrayList<>();
        String req = "SELECT * FROM ligne_transport";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(req)) {

            // Loop through the result set and create ligneTransport objects
            while (rs.next()) {
                ligneTransport ligne = new ligneTransport(
                        rs.getString("nom_ligne"),
                        rs.getString("zone_couverture"),
                        rs.getDouble("tarif"),
                        rs.getTime("horaire_depart"),
                        rs.getTime("horaire_arrivee"),
                        rs.getString("etat")
                );
                ligne.setIdLigne(rs.getInt("id_ligne"));  // Set the ID of the transport line
                lignesTransport.add(ligne);  // Add the object to the list
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation : " + e.getMessage());
        }
        return lignesTransport;
    }



}
