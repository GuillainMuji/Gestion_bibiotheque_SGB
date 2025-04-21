package org.example.deo;

import org.example.database.Livre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {

    // Méthode pour charger les livres depuis la base de données
    public static List<Livre> chargerLivres(Connection connection) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livre livre = new Livre(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("isbn"),
                        rs.getInt("annee"),
                        rs.getString("categorie"),
                        rs.getBoolean("disponible"));
                livres.add(livre);
            }
        }

        return livres;
    }

    // Méthode pour sauvegarder une liste de livres dans la base de données
    public static void sauvegarderLivres(Connection connection, List<Livre> livres) throws SQLException {
        String sql = "REPLACE INTO livres (id, titre, auteur, isbn, annee, disponible, categorie) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Livre livre : livres) {
                stmt.setInt(1, livre.getId());
                stmt.setString(2, livre.getTitre());
                stmt.setString(3, livre.getAuteur());
                stmt.setString(4, livre.getIsbn());
                stmt.setInt(5, livre.getAnnee());
                stmt.setBoolean(6, livre.isDisponible());
                stmt.setString(7, livre.getCategorie());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
