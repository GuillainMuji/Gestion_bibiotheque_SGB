package org.example.deo;

import org.example.database.Emprunt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {

    public static List<Emprunt> chargerEmprunts(Connection connection) throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Emprunt emprunt = new Emprunt(
                        rs.getInt("id"),
                        rs.getInt("livre_id"),
                        rs.getDate("date_emprunt").toLocalDate(),
                        rs.getDate("date_retour") != null ? rs.getDate("date_retour").toLocalDate() : null
                );
                emprunts.add(emprunt);
            }
        }

        return emprunts;
    }

    public static void sauvegarderEmprunts(Connection connection, List<Emprunt> emprunts) throws SQLException {
        String sql = "REPLACE INTO emprunts (id, livre_id, utilisateur_id, date_emprunt, date_retour) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Emprunt emprunt : emprunts) {
                stmt.setInt(1, emprunt.getLivreId());  // livre_id
                stmt.setInt(2, emprunt.getUtilisateurId());  // utilisateur_id
                stmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));  // date_emprunt
                if (emprunt.getDateRetour() != null) {
                    stmt.setDate(4, Date.valueOf(emprunt.getDateRetour()));  // date_retour
                } else {
                    stmt.setNull(4, Types.DATE);  // Si aucune date de retour, on met null
                }
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
