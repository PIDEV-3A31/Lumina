package com.esprit.services;

import com.esprit.models.Documents;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDocuments implements CrudMunicipalites<Documents>{
    private Connection connection;



    public ServiceDocuments() {
        connection = DataBase.getInstance().getConnection();
    }


    @Override
    public void ajouter(Documents document) {
        String req = "INSERT INTO documents (type_document, titre, description, date_creation, date_modification, chemin_fichier) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, document.getType_document());
            ps.setString(2, document.getTitre());
            ps.setString(3, document.getDescription());
            ps.setDate(4, new java.sql.Date(document.getDate_creation().getTime()));
            ps.setDate(5, new java.sql.Date(document.getDate_modification().getTime()));
            ps.setString(6, document.getChemin_fichier());
            ps.executeUpdate();
            System.out.println("Ajout du document réussi !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Documents document) {
        String req = "UPDATE documents SET type_document=?, titre=?, description=?, date_modification=?, chemin_fichier=? WHERE id_document=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, document.getType_document());
            ps.setString(2, document.getTitre());
            ps.setString(3, document.getDescription());
            ps.setDate(4, new java.sql.Date(document.getDate_modification().getTime()));
            ps.setString(5, document.getChemin_fichier());
            ps.setInt(6, document.getId_document());
            ps.executeUpdate();
            System.out.println("Modification du document réussie !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM documents WHERE id_document=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Suppression du document réussie !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Documents> consulter() {
        List<Documents> documents = new ArrayList<>();
        String req = "SELECT * FROM documents";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(req)) {
            while (rs.next()) {
                Documents document = new Documents();
                document.setId_document(rs.getInt("id_document"));
                document.setType_document(rs.getString("type_document"));
                document.setTitre(rs.getString("titre"));
                document.setDescription(rs.getString("description"));
                document.setDate_creation(rs.getDate("date_creation"));
                document.setDate_modification(rs.getDate("date_modification"));
                document.setChemin_fichier(rs.getString("chemin_fichier"));
                documents.add(document);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation : " + e.getMessage());
        }
        return documents;
    }

    public List<Documents> recupererDocumentsSelonIdUser(int idUser) {
        List<Documents> documents = new ArrayList<>();
        String req = "SELECT * FROM documents WHERE id_document IN (SELECT id_document FROM demandes WHERE id_utilisateur = ?)";

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setInt(1, idUser);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Documents document = new Documents();
                document.setId_document(rs.getInt("id_document"));
                document.setType_document(rs.getString("type_document"));
                document.setTitre(rs.getString("titre"));
                document.setDescription(rs.getString("description"));
                document.setDate_creation(rs.getDate("date_creation"));
                document.setDate_modification(rs.getDate("date_modification"));
                document.setChemin_fichier(rs.getString("chemin_fichier"));
                documents.add(document);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des documents de l'utilisateur " + idUser + " : " + e.getMessage());
        }
        return documents;
    }



    public Documents getDocumentForDemande(int id_demande) {
        Documents document = null;
        String req = "SELECT d.* FROM documents d " +
                "JOIN demandes dem ON d.id_document = dem.id_document " +
                "WHERE dem.id_demande = ?";

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setInt(1, id_demande);  // Remplacer ? par l'id de la demande
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Création du document à partir des données récupérées
                    document = new Documents();
                    document.setId_document(rs.getInt("id_document"));
                    document.setType_document(rs.getString("type_document"));
                    document.setTitre(rs.getString("titre"));
                    document.setDescription(rs.getString("description"));
                    document.setDate_creation(rs.getDate("date_creation"));
                    document.setDate_modification(rs.getDate("date_modification"));
                    document.setChemin_fichier(rs.getString("chemin_fichier"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du document pour la demande : " + e.getMessage());
        }

        return document;  // Retourne le document trouvé ou null si aucun document n'est associé à la demande
    }




}

