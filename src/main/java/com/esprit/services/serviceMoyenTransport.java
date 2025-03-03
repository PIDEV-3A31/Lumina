package com.esprit.services;

import com.esprit.models.moyenTransport;
import com.esprit.utils.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class serviceMoyenTransport implements CrudTransport<moyenTransport>{

    private Connection connection;

    public serviceMoyenTransport() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(moyenTransport obj) {
        String req = "INSERT INTO moyen_transport (id_ligne, type_transport, capacite_max, immatriculation, etat, places_reservees) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, obj.getIdLigne());
            ps.setString(2, obj.getTypeTransport());
            ps.setInt(3, obj.getCapaciteMax());
            ps.setString(4, obj.getImmatriculation());
            ps.setString(5, obj.getEtat());
            ps.setInt(6, 0);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Moyen de transport ajouté avec succès !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du moyen de transport : " + e.getMessage());
        }
    }

    @Override
    public void modifier(moyenTransport obj) {
        String sql = "UPDATE moyen_transport SET id_ligne = ?, type_transport = ?, capacite_max = ?, immatriculation = ?, etat = ? WHERE id_moyenTransport = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, obj.getIdLigne());
            ps.setString(2, obj.getTypeTransport());
            ps.setInt(3, obj.getCapaciteMax());
            ps.setString(4, obj.getImmatriculation());
            ps.setString(5, obj.getEtat());
            ps.setInt(6, obj.getIdTransport()); // Condition pour modifier l'élément spécifique


            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(" Moyen de transport modifié avec succès !");
            } else {
                System.out.println(" Aucun enregistrement trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la modification du moyen de transport : " + e.getMessage());
        }
    }


    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM moyen_transport WHERE id_moyenTransport = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(" Moyen de transport supprimé avec succès !");
            } else {
                System.out.println(" Aucun enregistrement trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la suppression du moyen de transport : " + e.getMessage());
        }
    }


    @Override
    public List<moyenTransport> consulter() {
        List<moyenTransport> listeMoyens = new ArrayList<>();
        String sql = "SELECT * FROM moyen_transport";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                moyenTransport moyen = new moyenTransport(
                        rs.getInt("id_moyenTransport"),
                        rs.getInt("id_ligne"),
                        rs.getString("type_transport"),
                        rs.getInt("capacite_max"),
                        rs.getString("immatriculation"),
                        rs.getString("etat"),
                        rs.getInt("places_reservees")
                );
                listeMoyens.add(moyen);
            }

        } catch (SQLException e) {
            System.err.println(" Erreur lors de la consultation des moyens de transport : " + e.getMessage());
        }

        return listeMoyens;
    }

    public List<moyenTransport> getMoyensByLigneId(int idLigne) {
        List<moyenTransport> listeMoyens = new ArrayList<>();
        String sql = "SELECT * FROM moyen_transport WHERE id_ligne = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idLigne);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    moyenTransport moyen = new moyenTransport(
                            rs.getInt("id_moyenTransport"),
                            rs.getInt("id_ligne"),
                            rs.getString("type_transport"),
                            rs.getInt("capacite_max"),
                            rs.getString("immatriculation"),
                            rs.getString("etat"),
                            rs.getInt("places_reservees")
                    );
                    listeMoyens.add(moyen);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des moyens de transport pour la ligne " + idLigne + " : " + e.getMessage());
        }

        return listeMoyens;
    }


}