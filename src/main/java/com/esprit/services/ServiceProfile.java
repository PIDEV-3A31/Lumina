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
        String sql = "INSERT INTO profile (name_u, email_u, phone_u, role, id_user, image_u) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getName_u());
            stmt.setString(2, p.getEmail_u());
            stmt.setInt(3, p.getPhone_u());
            stmt.setString(4, p.getRole());
            stmt.setInt(5, p.getId_user());
            stmt.setString(6, p.getImage_u());

            stmt.executeUpdate();
            System.out.println("Profil ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du profil : " + e.getMessage());
        }
    }

    @Override
    public void modifer(profile p, int id) {
        String sql = "UPDATE profile SET name_u = ?, email_u = ?, phone_u = ?, role = ?, id_user = ?, image_u = ? WHERE id_profile = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getName_u());
            stmt.setString(2, p.getEmail_u());
            stmt.setInt(3, p.getPhone_u());
            stmt.setString(4, p.getRole());
            stmt.setInt(5, p.getId_user());
            stmt.setString(6, p.getImage_u());
            stmt.setInt(7, id);

            stmt.executeUpdate();
            System.out.println("Profil modifié avec succès !");
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
        String sql = "SELECT * FROM profile";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                profile profile = new profile(
                    rs.getInt("id_user"),
                    rs.getInt("id_profile"),
                    rs.getString("name_u"),
                    rs.getString("email_u"),
                    rs.getInt("phone_u"),
                    rs.getString("role"),
                    rs.getString("image_u")
                );
                profile.setCreated_at(rs.getTimestamp("created_at"));
                profile.setUpdated_at(rs.getTimestamp("updated_at"));
                profiles.add(profile);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des profils : " + e.getMessage());
        }

        return profiles;
    }

    public String getRoleByUserId(int userId) {
        String sql = "SELECT role FROM profile WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du rôle : " + e.getMessage());
        }
        return null;
    }

    public profile getProfileByUserId(int userId) {
        String sql = "SELECT * FROM profile WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                profile p = new profile(
                    rs.getInt("id_user"),
                    rs.getInt("id_profile"),
                    rs.getString("name_u"),
                    rs.getString("email_u"),
                    rs.getInt("phone_u"),
                    rs.getString("role"),
                    rs.getString("image_u")
                );
                p.setCreated_at(rs.getTimestamp("created_at"));
                p.setUpdated_at(rs.getTimestamp("updated_at"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du profil : " + e.getMessage());
        }
        return null;
    }

    public profile getOneById(int profileId) {
        String sql = "SELECT * FROM profile WHERE id_profile = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, profileId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                profile p = new profile(
                    rs.getInt("id_user"),
                    rs.getInt("id_profile"),
                    rs.getString("name_u"),
                    rs.getString("email_u"),
                    rs.getInt("phone_u"),
                    rs.getString("role"),
                    rs.getString("image_u")
                );
                p.setCreated_at(rs.getTimestamp("created_at"));
                p.setUpdated_at(rs.getTimestamp("updated_at"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du profil : " + e.getMessage());
        }
        return null;
    }
    public String getImagePathByUserId(int userId) {
        String imagePath = null;
        String query = "SELECT image_u FROM profile WHERE id_profile = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                imagePath = rs.getString("image_u");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imagePath; // Retourne `null` si l'utilisateur n'a pas d'image
    }

    }
