package org.example.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FichierMySQL {

    // Charger tous les utilisateurs depuis la base
    public List<Utilisateur> chargerUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateurs";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");

                utilisateurs.add(new Utilisateur(id, nom, email, password, role));
            }
            System.out.println("Utilisateurs chargés : " + utilisateurs.size());
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des utilisateurs : " + e.getMessage());
        }

        return utilisateurs;
    }

    // Sauvegarder les utilisateurs dans la base
    public void sauvegarderUtilisateurs(List<Utilisateur> utilisateurs) {
        String query = "INSERT INTO utilisateurs (id, nom, email, password, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (Utilisateur utilisateur : utilisateurs) {
                stmt.setInt(1, utilisateur.getId());
                stmt.setString(2, utilisateur.getNom());
                stmt.setString(3, utilisateur.getEmail());
                stmt.setString(4, utilisateur.getPassword());
                stmt.setString(5, utilisateur.getRole());
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Utilisateurs sauvegardés avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde des utilisateurs : " + e.getMessage());
        }
    }

    // Authentifier un utilisateur
    public Utilisateur authentifierUtilisateur(String email, String password) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String role = rs.getString("role");

                // Création d'une instance en fonction du rôle
                switch (role.toLowerCase()) {
                    case "admin":
                        return new Admin(id, nom, email, password, role);
                    case "bibliothecaire":
                        return new Bibliothecaire(id, nom, email, password, role);
                    case "lecteur":
                        return new Lecteur(id, nom, email, password, role);
                    default:
                        return new Utilisateur(id, nom, email, password, role);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
        }
        return null;
    }

    // Mettre à jour le rôle d'un utilisateur
    public void mettreAJourRole(int id, String nouveauRole) {
        String sql = "UPDATE utilisateurs SET role = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nouveauRole);
            stmt.setInt(2, id);

            int lignesModifiees = stmt.executeUpdate();
            if (lignesModifiees > 0) {
                System.out.println("Rôle mis à jour avec succès.");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du rôle : " + e.getMessage());
        }
    }

    // Charger tous les livres
    public List<Livre> chargerLivres() {
        List<Livre> livres = new ArrayList<>();
        String query = "SELECT * FROM livres";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String auteur = rs.getString("auteur");
                String isbn = rs.getString("isbn");
                int annee = rs.getInt("annee");

                // Vérifie si la colonne 'categorie' existe
                String categorie;
                try {
                    categorie = rs.getString("categorie");
                } catch (SQLException e) {
                    categorie = "Inconnue"; // Valeur par défaut si la colonne n'existe pas
                }

                boolean disponible;
                try {
                    disponible = rs.getBoolean("disponible");
                } catch (SQLException e) {
                    disponible = true; // Par défaut à true si non défini
                }

                livres.add(new Livre(id, titre, auteur, isbn, annee, categorie, disponible));
            }

            // Inverser la liste des livres (affiche les plus récents d'abord)
            Collections.reverse(livres);

            System.out.println("📚 Livres chargés avec succès : " + livres.size());
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors du chargement des livres : " + e.getMessage());
            e.printStackTrace();
        }

        return livres;
    }


    public Livre rechercherLivre(String titre) {
        Livre livre = null;
        String sql = "SELECT * FROM livres WHERE titre = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String auteur = rs.getString("auteur");
                String isbn = rs.getString("isbn");
                int annee = rs.getInt("annee");
                String categorie = rs.getString("categorie");
                boolean disponible = rs.getBoolean("disponible");

                livre = new Livre(id, titre, auteur, isbn, annee, categorie, disponible);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livre;
    }

    public boolean retournerLivre(int idLivre) {
        String sql = "UPDATE livres SET disponible = TRUE WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLivre);
            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Livre> listerLivresDisponibles() {
        List<Livre> livresDisponibles = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE disponible = true";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String auteur = rs.getString("auteur");
                String isbn = rs.getString("isbn");
                int annee = rs.getInt("annee");
                String categorie = rs.getString("categorie");
                boolean disponible = rs.getBoolean("disponible");

                Livre livre = new Livre(id, titre, auteur, isbn, annee, categorie, disponible);
                livresDisponibles.add(livre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livresDisponibles;
    }

    // Sauvegarder les livres
    public void sauvegarderLivres(List<Livre> livres) {
        String query = "INSERT INTO livres (id, titre, auteur, isbn, annee, disponible) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (Livre livre : livres) {
                stmt.setInt(1, livre.getId());
                stmt.setString(2, livre.getTitre());
                stmt.setString(3, livre.getAuteur());
                stmt.setString(4, livre.getIsbn());
                stmt.setInt(5, livre.getAnnee());
                stmt.setBoolean(6, livre.isDisponible());
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Livres sauvegardés avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde des livres : " + e.getMessage());
        }
    }

    // Charger les emprunts
    public List<Emprunt> chargerEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        String query = "SELECT * FROM emprunts";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int utilisateurId = rs.getInt("utilisateur_id");
                int livreId = rs.getInt("livre_id");
                LocalDate dateEmprunt = rs.getDate("date_emprunt").toLocalDate();

                LocalDate dateRetour = null;
                if (rs.getDate("date_retour") != null) {
                    dateRetour = rs.getDate("date_retour").toLocalDate();
                }

                Emprunt emprunt = new Emprunt(utilisateurId, livreId, dateEmprunt, dateRetour);
                emprunts.add(emprunt);
            }

            System.out.println("Emprunts chargés : " + emprunts.size());
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des emprunts : " + e.getMessage());
        }

        return emprunts;
    }
    public boolean emprunterLivre(int idLivre, int idLecteur) {
        String sqlUpdate = "UPDATE livres SET disponible = false WHERE id = ?";
        String sqlInsertEmprunt = "INSERT INTO emprunts (id_lecteur, id_livre, date_emprunt) VALUES (?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Démarre une transaction

            // 1. Mettre à jour l'état du livre
            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
                updateStmt.setInt(1, idLivre);
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Enregistrer l'emprunt
            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsertEmprunt)) {
                insertStmt.setInt(1, idLecteur);
                insertStmt.setInt(2, idLivre);
                insertStmt.executeUpdate();
            }

            conn.commit(); // Valide la transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Générer un nouvel ID pour un utilisateur
    public int genererNouvelId() {
        String query = "SELECT MAX(id) AS max_id FROM utilisateurs";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // valeur par défaut si la table est vide
    }

    // Ajouter un utilisateur
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        String query = "INSERT INTO utilisateurs (id, nom, email, password, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, utilisateur.getId());
            stmt.setString(2, utilisateur.getNom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getPassword());
            stmt.setString(5, utilisateur.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un utilisateur
    public void supprimerUtilisateur(int id) {
        String query = "DELETE FROM utilisateurs WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sauvegarder les emprunts
    public void sauvegarderEmprunts(List<Emprunt> emprunts) {
        String query = "INSERT INTO emprunts (utilisateur_id, livre_id, date_emprunt, date_retour) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (Emprunt emprunt : emprunts) {
                stmt.setInt(1, emprunt.getUtilisateurId());
                stmt.setInt(2, emprunt.getLivreId());
                stmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
                stmt.setDate(4, emprunt.getDateRetour() != null ? Date.valueOf(emprunt.getDateRetour()) : null);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Emprunts sauvegardés avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde des emprunts : " + e.getMessage());
        }
    }
}
