package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    // Remplacez ces valeurs par celles de votre base de données
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_bibliotheque";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // mot de passe vide pour XAMPP par défaut

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // Méthode pour obtenir une connexion à la base de données
    public static Connection getConnection() throws SQLException {
        try {
            // Enregistrer le driver MySQL (optionnel, peut être géré automatiquement)
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL non trouvé", e);
        }
    }
}
