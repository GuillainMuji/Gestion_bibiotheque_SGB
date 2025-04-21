package org.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Categorie {
    private int id;
    private String nom;

    // Constructeurs
    public Categorie() {}

    public Categorie(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Categorie(String nom) {
        this.nom = nom;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }

    // ✅ Charger toutes les catégories
    public static List<Categorie> chargerCategories(Connection conn) {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT id, nom FROM categories";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                categories.add(new Categorie(id, nom));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des catégories : " + e.getMessage());
        }

        return categories;
    }

    // ✅ Obtenir une catégorie par son nom
    public static Categorie getCategorieByNom(Connection conn, String nomRecherche) {
        String sql = "SELECT id, nom FROM categories WHERE LOWER(nom) = LOWER(?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomRecherche);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Categorie(rs.getInt("id"), rs.getString("nom"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la catégorie : " + e.getMessage());
        }

        return null;
    }

    // ✅ Ajouter une nouvelle catégorie si elle n'existe pas encore
    public static int ajouterCategorie(Connection conn, String nomCategorie) {
        Categorie existante = getCategorieByNom(conn, nomCategorie);
        if (existante != null) {
            return existante.getId(); // Retourne l'ID existant
        }

        String sql = "INSERT INTO categories (nom) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nomCategorie);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1); // Retourne le nouvel ID
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
        }

        return -1;
    }

    // ✅ Vérifie si une catégorie existe
    public static boolean existeCategorie(Connection conn, String nomCategorie) {
        return getCategorieByNom(conn, nomCategorie) != null;
    }
}
