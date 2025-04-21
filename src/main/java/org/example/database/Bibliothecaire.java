package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bibliothecaire extends Utilisateur {

    public Bibliothecaire() {
        super();
    }

    public Bibliothecaire(int id, String nom, String email, String password, String role) {
        super(id, nom, email, password, role);
    }

    // Méthode pour ajouter un livre dans la base de données avec la catégorie
    public void ajouterLivre(Bibliotheque biblio, Livre livre) {
        int categorieId = obtenirCategorieId(livre.getCategorie());

        if (categorieId == -1) {
            System.out.println("❌ Catégorie introuvable !");
            return;
        }

        String query = "INSERT INTO livres (titre, auteur, isbn, annee, categorie_id, disponible) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getIsbn());
            pstmt.setInt(4, livre.getAnnee());
            pstmt.setInt(5, categorieId);
            pstmt.setBoolean(6, livre.isDisponible());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Livre ajouté avec succès dans la base de données !");
                biblio.ajouterLivre(livre); // Ajouter aussi dans la liste en mémoire
            } else {
                System.out.println("❌ Échec de l'ajout du livre.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du livre : " + e.getMessage());
            e.printStackTrace();
        }
    }



    // Méthode pour obtenir l'ID de la catégorie à partir de son nom, ou créer la catégorie si elle n'existe pas
    private int obtenirCategorieId(String nomCategorie) {
        String selectQuery = "SELECT id FROM categorie WHERE nom = ?";
        String insertQuery = "INSERT INTO categorie (nom) VALUES (?)";

        try (Connection connection = DBConnection.getConnection()) {
            // Vérifier si la catégorie existe
            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
                selectStmt.setString(1, nomCategorie);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

            // Si elle n'existe pas, on la crée
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, nomCategorie);
                int affectedRows = insertStmt.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        System.out.println("📁 Catégorie '" + nomCategorie + "' créée automatiquement.");
                        return generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération ou création de la catégorie : " + e.getMessage());
        }

        return -1;
    }

    // Méthode pour supprimer un livre de la base de données
    public void supprimerLivre(Bibliotheque biblio, int idLivre) {
        String query = "DELETE FROM livres WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idLivre);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livre supprimé : ID " + idLivre);
            } else {
                System.out.println("Aucun livre trouvé avec l'ID : " + idLivre);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livre : " + e.getMessage());
        }
    }

    // Méthode pour rechercher un livre dans la base de données par son titre
    public Livre rechercherLivre(Bibliotheque biblio, String titre) {
        String query = "SELECT l.id, l.titre, l.auteur, l.isbn, l.annee, c.nom AS categorie " +
                "FROM livres l " +
                "JOIN categorie c ON l.categorie_id = c.id " +
                "WHERE l.titre = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, titre);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String auteur = resultSet.getString("auteur");
                String isbn = resultSet.getString("isbn");
                int annee = resultSet.getInt("annee");
                String categorie = resultSet.getString("categorie");
                boolean disponible = true;  // Supposons que le livre est disponible (ajouter la logique si nécessaire)
                return new Livre(id, titre, auteur, isbn, annee, categorie, disponible);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du livre : " + e.getMessage());
        }
        return null;
    }

    // Méthode pour lister tous les livres de la base de données avec leur catégorie
    public void listerLivres(Bibliotheque biblio) {
        String query = "SELECT l.id, l.titre, l.auteur, l.isbn, l.annee, c.nom AS categorie " +
                "FROM livres l " +
                "JOIN categorie c ON l.categorie_id = c.id";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String auteur = resultSet.getString("auteur");
                String isbn = resultSet.getString("isbn");
                int annee = resultSet.getInt("annee");
                String categorie = resultSet.getString("categorie");
                System.out.println("ID: " + id + ", Titre: " + titre + ", Auteur: " + auteur + ", ISBN: " + isbn + ", Année: " + annee + ", Catégorie: " + categorie);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage des livres : " + e.getMessage());
        }
    }



    public void listerLivresDisponibles(Bibliotheque biblio) {
        // Parcourez tous les livres et affichez ceux qui sont disponibles
        for (Livre livre : biblio.getLivres()) {
            if (livre.isDisponible()) {
                System.out.println("Titre: " + livre.getTitre() + ", Auteur: " + livre.getAuteur());
            }
        }
    }
    // Méthode pour rechercher des livres par catégorie
    public List<Livre> rechercherParCategorie(Bibliotheque biblio, String nomCategorie) {
        String query = "SELECT l.id, l.titre, l.auteur, l.isbn, l.annee, c.nom AS categorie " +
                "FROM livres l " +
                "JOIN categorie c ON l.categorie_id = c.id " +
                "WHERE c.nom = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nomCategorie);
            ResultSet resultSet = statement.executeQuery();

            List<Livre> livres = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String auteur = resultSet.getString("auteur");
                String isbn = resultSet.getString("isbn");
                int annee = resultSet.getInt("annee");
                String categorie = resultSet.getString("categorie");
                boolean disponible = true;  // Logique à ajouter pour la disponibilité
                livres.add(new Livre(id, titre, auteur, isbn, annee, categorie, disponible));
            }
            return livres;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par catégorie : " + e.getMessage());
        }
        return new ArrayList<>(); // Retourne une liste vide en cas d'erreur
    }
}
