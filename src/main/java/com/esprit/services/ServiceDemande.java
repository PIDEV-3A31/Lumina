package com.esprit.services;

import com.esprit.models.Demandes;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDemande implements CrudMunicipalites<Demandes> {

    private Connection connection;

    public ServiceDemande() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Demandes demande) {
        String query = "INSERT INTO Demandes (id_utilisateur, id_document, type_document, date_demande, status_demande, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, demande.getId_utilisateur());
            ps.setInt(2, demande.getId_document());
            ps.setString(3, demande.getType_document());
            ps.setDate(4, new java.sql.Date(demande.getDate_demande().getTime()));
            ps.setString(5, demande.getStatut_demande());
            ps.setString(6, demande.getDescription());
            ps.executeUpdate();
            System.out.println(" Demande ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println(" Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Demandes demande) {
        String query = "UPDATE Demandes SET id_utilisateur = ?, id_document = ?, type_document = ?, date_demande = ?, status_demande = ?, description = ? WHERE id_demande = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, demande.getId_utilisateur());
            ps.setInt(2, demande.getId_document());
            ps.setString(3, demande.getType_document());
            ps.setDate(4, new java.sql.Date(demande.getDate_demande().getTime()));
            ps.setString(5, demande.getStatut_demande());
            ps.setString(6, demande.getDescription());
            ps.setInt(7, demande.getId_demande());
            ps.executeUpdate();
            System.out.println(" Demande modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println(" Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String query = "DELETE FROM Demandes WHERE id_demande = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println(" Demande supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println(" Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Demandes> consulter() {
        List<Demandes> demandes = new ArrayList<>();
        String query = "SELECT * FROM Demandes";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Demandes d = new Demandes();
                d.setId_demande(rs.getInt("id_demande"));
                d.setId_utilisateur(rs.getInt("id_utilisateur"));
                d.setId_document(rs.getInt("id_document"));
                d.setType_document(rs.getString("type_document"));
                d.setDate_demande(rs.getDate("date_demande"));
                d.setStatut_demande(rs.getString("status_demande"));
                d.setDescription(rs.getString("description"));
                demandes.add(d);
            }
        } catch (SQLException e) {
            System.out.println(" Erreur lors de la consultation : " + e.getMessage());
        }
        return demandes;
    }

    public List<Demandes> getDemandesByUserId(int idUtilisateur) {
        List<Demandes> demandes = new ArrayList<>();
        String query = "SELECT * FROM Demandes WHERE id_utilisateur = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idUtilisateur);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Demandes d = new Demandes();
                    d.setId_demande(rs.getInt("id_demande"));
                    d.setId_utilisateur(rs.getInt("id_utilisateur"));
                    d.setId_document(rs.getInt("id_document"));
                    d.setType_document(rs.getString("type_document"));
                    d.setDate_demande(rs.getDate("date_demande"));
                    d.setStatut_demande(rs.getString("status_demande"));
                    d.setDescription(rs.getString("description"));
                    demandes.add(d);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des demandes de l'utilisateur : " + e.getMessage());
        }

        return demandes;
    }

}
