package com.esprit.services;

import com.esprit.models.ligneTransport;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class serviceLigneTransport implements CrudTransport<ligneTransport> {

    private Connection connection;

    public serviceLigneTransport() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(ligneTransport ligneTransport) {
        String req = "INSERT INTO ligne_transport (nom_ligne, zone_couverture, tarif, horaire_depart, horaire_arrivee, etat, lieux_depart, lieux_arrive) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, ligneTransport.getNomLigne());
            ps.setString(2, ligneTransport.getZoneCouverture());
            ps.setDouble(3, ligneTransport.getTarif());
            ps.setTime(4, new java.sql.Time(ligneTransport.getHoraireDepart().getTime()));
            ps.setTime(5, new java.sql.Time(ligneTransport.getHoraireArrivee().getTime()));
            ps.setString(6, ligneTransport.getEtat());
            ps.setString(7, ligneTransport.getLieuxDepart());  // 🚀 Ajout lieux départ
            ps.setString(8, ligneTransport.getLieuxArrive());  // 🚀 Ajout lieux arrivée

            ps.executeUpdate();
            System.out.println("✅ Ligne de transport ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void modifier(ligneTransport ligneTransport) {
        String req = "UPDATE ligne_transport SET nom_ligne = ?, zone_couverture = ?, tarif = ?, horaire_depart = ?, horaire_arrivee = ?, etat = ?, lieux_depart = ?, lieux_arrive = ? " +
                "WHERE id_ligne = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, ligneTransport.getNomLigne());
            ps.setString(2, ligneTransport.getZoneCouverture());
            ps.setDouble(3, ligneTransport.getTarif());
            ps.setTime(4, new java.sql.Time(ligneTransport.getHoraireDepart().getTime()));
            ps.setTime(5, new java.sql.Time(ligneTransport.getHoraireArrivee().getTime()));
            ps.setString(6, ligneTransport.getEtat());
            ps.setString(7, ligneTransport.getLieuxDepart());  // 🚀 Ajout lieux départ
            ps.setString(8, ligneTransport.getLieuxArrive());  // 🚀 Ajout lieux arrivée
            ps.setInt(9, ligneTransport.getIdLigne());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Ligne de transport modifiée avec succès !");
            } else {
                System.out.println("⚠️ Aucune ligne trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM ligne_transport WHERE id_ligne = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Ligne de transport supprimée avec succès !");
            } else {
                System.out.println("⚠️ Aucune ligne trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    public List<ligneTransport> consulter() {
        List<ligneTransport> lignesTransport = new ArrayList<>();
        String req = "SELECT * FROM ligne_transport";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(req)) {

            while (rs.next()) {
                ligneTransport ligne = new ligneTransport(
                        rs.getInt("id_ligne"),
                        rs.getString("nom_ligne"),
                        rs.getString("zone_couverture"),
                        rs.getDouble("tarif"),
                        rs.getTime("horaire_depart"),
                        rs.getTime("horaire_arrivee"),
                        rs.getString("etat"),
                        rs.getString("lieux_depart"),
                        rs.getString("lieux_arrive")
                );
                lignesTransport.add(ligne);
            }
        } catch (SQLException e) {
            System.out.println(" Erreur lors de la consultation : " + e.getMessage());
        }
        return lignesTransport;
    }
    public double getTarifByLigneId(int idLigne) {
        double tarif = 0.0;
        String query = "SELECT tarif FROM ligne_transport WHERE id_ligne = ?";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idLigne);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tarif = rs.getDouble("tarif");
                System.out.println(tarif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarif;
    }

    public Map<Integer, String> getLignesMap() {
        Map<Integer, String> lignesMap = new HashMap<>();
        String req = "SELECT id_ligne, nom_ligne FROM ligne_transport";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(req)) {

            while (rs.next()) {
                lignesMap.put(rs.getInt("id_ligne"), rs.getString("nom_ligne"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des lignes : " + e.getMessage());
        }
        return lignesMap;
    }

    public String getNomLigneById(int idLigne) {
        serviceLigneTransport slt = new serviceLigneTransport();
        Map<Integer, String> lignesMap = slt.getLignesMap();  // Récupère la map contenant idLigne -> nomLigne
        return lignesMap.getOrDefault(idLigne, "Inconnu");  // Retourne le nom de la ligne ou "Inconnu" si l'ID n'existe pas
    }





}