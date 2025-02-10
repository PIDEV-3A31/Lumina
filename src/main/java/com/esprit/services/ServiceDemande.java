package com.esprit.services;

import com.esprit.models.Demandes;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDemande implements CrudMunicipalites<Demandes>{

    private Connection connection;

    public ServiceDemande() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Demandes demande) {
        String query = "INSERT INTO Demandes (id_utilisateur, id_document, date_demande, status_demande) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, demande.getId_utilisateur());
            ps.setInt(2, demande.getId_document());
            ps.setDate(3, new java.sql.Date(demande.getDate_demande().getTime()));
            ps.setString(4, demande.getStatut_demande());
            ps.executeUpdate();
            System.out.println("Demande ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }

    }

    @Override
    public void modifier(Demandes demande) {
        String query = "UPDATE Demandes SET id_utilisateur = ?, id_document = ?, date_demande = ?, status_demande = ? WHERE id_demande = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, demande.getId_utilisateur());
            ps.setInt(2, demande.getId_document());
            ps.setDate(3, new java.sql.Date(demande.getDate_demande().getTime()));
            ps.setString(4, demande.getStatut_demande());
            ps.setInt(5, demande.getId_demande());
            ps.executeUpdate();
            System.out.println("Demande modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String query = "DELETE FROM Demandes WHERE id_demande = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Demande supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
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
                d.setDate_demande(rs.getDate("date_demande"));
                d.setStatut_demande(rs.getString("status_demande"));
                demandes.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation : " + e.getMessage());
        }
        return demandes;
    }
}
