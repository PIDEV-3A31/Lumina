package com.esprit.services;

import com.esprit.models.profile;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProfile implements CrudService<profile>{
    private Connection connection = DataBase.getInstance().getConnection();

    @Override
    public void ajouter(profile p) {
            String sql = "INSERT INTO profile (name_u, email_u, phone_u, role, id_user) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, p.getName_u());
                stmt.setString(2, p.getEmail_u());
                stmt.setInt(3, p.getPhone_u());
                stmt.setString(4, p.getRole());
                stmt.setInt(5, p.getId_user());

                // Exécuter la requête
                stmt.executeUpdate();
                System.out.println("Profil ajouté avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout du profil : " + e.getMessage());
            }

    }

    @Override
    public void modifer(profile p, int id) {
        String sql = "UPDATE profile SET name_u = ?, email_u = ?, phone_u = ?, role = ?, id_user = ? WHERE id_profile = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getName_u());
            stmt.setString(2, p.getEmail_u());
            stmt.setInt(3, p.getPhone_u());
            stmt.setString(4, p.getRole());
            stmt.setInt(5, p.getId_user()); // Assure-toi que cet id_user existe dans la table 'user'
            stmt.setInt(6, id); // L'ID du profil à modifier

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Profil modifié avec succès !");
            } else {
                System.out.println("Aucun profil trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du profil : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM profile WHERE id_profile = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Profil supprimé avec succès !");
            } else {
                System.out.println("Aucun profil trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du profil : " + e.getMessage());
        }

    }

    @Override
    public List<profile> afficher() {
            List<profile> profiles = new ArrayList<>();
            String sql = "SELECT name_u, email_u, phone_u, role, update_at FROM profile";

            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String name = rs.getString("name_u");
                    String email = rs.getString("email_u");
                    int phone = rs.getInt("phone_u");
                    String role = rs.getString("role");
                    Timestamp updatedAt = rs.getTimestamp("update_at");

                    // Crée un nouvel objet Profile et l'ajoute à la liste
                    profile profile = new profile(name, email, phone, role, updatedAt);
                    profiles.add(profile);
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'affichage des profils : " + e.getMessage());
            }

            return profiles;
    }
}
