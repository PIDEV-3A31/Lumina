package com.esprit.services;

import com.esprit.models.user;
import com.esprit.utils.DataBase;
import com.esprit.utils.PasswordEncryption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements CrudService<user> {
    private Connection connection = DataBase.getInstance().getConnection();

    @Override
    public void ajouter(user u) {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try {
            // Hasher le mot de passe avant de le stocker
            String hashedPassword = PasswordEncryption.encryptPass(u.getPassword());
            System.out.println(hashedPassword);
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, u.getUsername());
            stmt.setString(2, hashedPassword); // Stocker le mot de passe haché
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @Override
    public void modifer(user u, int id) {
        String sql = "UPDATE user SET username = ?, password = ? WHERE id_user = ?";
        try {
            // Hasher le nouveau mot de passe
            String hashedPassword = PasswordEncryption.encryptPass(u.getPassword());
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, u.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
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
                throw new SQLException("Aucun utilisateur trouvé avec l'id : " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression : " + e.getMessage());
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

    public int ajouterAvecId(user u) throws Exception {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        int generatedId = -1;
        
        // Hasher le mot de passe avant de le stocker
        String hashedPassword = PasswordEncryption.encryptPass(u.getPassword());
        System.out.println("Mot de passe haché : " + hashedPassword);

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, u.getUsername());
            stmt.setString(2, hashedPassword);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
        return generatedId;
    }

    public boolean verifierUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // retourne true si username existe
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du username : " + e.getMessage());
            return false;
        }
    }

    public user verifierLogin(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Récupérer le mot de passe haché de la base de données
                String hashedPasswordFromDB = rs.getString("password");
                
                try {
                    // Hasher le mot de passe saisi et le comparer avec celui de la base
                    String hashedInputPassword = PasswordEncryption.encryptPass(password);
                    if (hashedPasswordFromDB.equals(hashedInputPassword)) {
                        user u = new user();
                        u.setId(rs.getInt("id_user"));
                        u.setUsername(rs.getString("username"));
                        u.setPassword(hashedPasswordFromDB);
                        u.setIs_2fa_enabled(rs.getBoolean("is_2fa_enabled"));
                        u.setSecret_Key(rs.getString("secret_key"));
                        return u;
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors du hachage : " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification : " + e.getMessage());
        }
        return null;
    }

    public user getUserById(int id) {
        String sql = "SELECT * FROM user WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new user(
                    rs.getInt("id_user"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    public boolean isUsernameUnique(String username, Integer excludeUserId) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        if (excludeUserId != null) {
            sql += " AND id_user != ?";
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            if (excludeUserId != null) {
                stmt.setInt(2, excludeUserId);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du username : " + e.getMessage());
        }
        return false;
    }

    public boolean isValidPassword(String password) {
        // Vérifier si le mot de passe respecte les critères
        return password != null && 
               password.length() >= 3 && 
               password.length() <= 20 && 
               password.matches(".*[A-Z].*");
    }

    public user getUserByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new user(
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    public void enable2FA(int userId, String secretKey) {
        String sql = "UPDATE user SET secret_key = ?, is_2fa_enabled = TRUE WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, secretKey);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'activation de la 2FA : " + e.getMessage());
        }
    }

    public void disable2FA(int userId) {
        String sql = "UPDATE user SET secret_key = NULL, is_2fa_enabled = FALSE WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la désactivation de la 2FA : " + e.getMessage());
        }
    }

    public void updatePassword(int userId, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE id_user = ?";
        try {
            // Hasher le nouveau mot de passe
            String hashedPassword = PasswordEncryption.encryptPass(newPassword);
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
        }
    }
}
