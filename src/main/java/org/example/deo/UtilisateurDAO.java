package org.example.deo;

import org.example.database.Utilisateur;
import org.example.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    // Ajouter un utilisateur
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (nom, email, mot_de_passe, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getPassword());
            stmt.setString(4, utilisateur.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    // Récupérer tous les utilisateurs
    public static List<Utilisateur> chargerUtilisateurs(Connection conn) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("role")
                );
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    // Sauvegarder ou mettre à jour les utilisateurs
    public static void sauvegarderUtilisateurs(Connection conn, List<Utilisateur> utilisateurs) {
        String sqlInsert = "INSERT INTO utilisateurs (id, nom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE utilisateurs SET nom = ?, email = ?, mot_de_passe = ?, role = ? WHERE id = ?";

        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {

            for (Utilisateur utilisateur : utilisateurs) {
                // Vérifier si l'utilisateur existe déjà en base
                if (utilisateur.getId() > 0) {
                    // Mettre à jour l'utilisateur
                    stmtUpdate.setString(1, utilisateur.getNom());
                    stmtUpdate.setString(2, utilisateur.getEmail());
                    stmtUpdate.setString(3, utilisateur.getPassword());
                    stmtUpdate.setString(4, utilisateur.getRole());
                    stmtUpdate.setInt(5, utilisateur.getId());
                    stmtUpdate.executeUpdate();
                } else {
                    // Ajouter l'utilisateur
                    stmtInsert.setInt(1, utilisateur.getId());
                    stmtInsert.setString(2, utilisateur.getNom());
                    stmtInsert.setString(3, utilisateur.getEmail());
                    stmtInsert.setString(4, utilisateur.getPassword());
                    stmtInsert.setString(5, utilisateur.getRole());
                    stmtInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde des utilisateurs : " + e.getMessage());
        }
    }

    // Rechercher un utilisateur par email
    public Utilisateur trouverParEmail(String email) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par email : " + e.getMessage());
        }
        return null;
    }

    // Supprimer un utilisateur
    public void supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    // Mettre à jour les infos d’un utilisateur
    public void mettreAJourUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET nom = ?, email = ?, mot_de_passe = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getPassword());
            stmt.setString(4, utilisateur.getRole());
            stmt.setInt(5, utilisateur.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }
}
