package com.esprit.services;

import com.esprit.models.user;
import com.esprit.utils.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements CrudService<user> {
    private Connection connection = DataBase.getInstance().getConnection();

    @Override
    public void ajouter(user u) {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utilisateur ajouté avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void modifer(user u,int id) {
        String sql = "UPDATE user SET username = ?, password = ? WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setInt(3, id);  // Ancien username pour identifier l'utilisateur

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur modifié avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'id : " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
        }

    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM user WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Utilisateur supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'id' : " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }

    }

    @Override
    public List<user> afficher() {
        List<user> users = new ArrayList<>();
        String sql = "SELECT username, password FROM user";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");

                // Crée un nouvel objet User et l'ajoute à la liste
                user user = new user(username, password);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'affichage des utilisateurs : " + e.getMessage());
        }

        return users;
    }

}
