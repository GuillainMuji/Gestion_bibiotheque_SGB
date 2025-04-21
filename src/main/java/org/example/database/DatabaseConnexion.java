package org.example.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnexion {

    private static Connection connection;

    public static Connection getConnection() {
        if (connection != null) return connection;

        try (InputStream input = DatabaseConnexion.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("❌ Fichier 'config.properties' non trouvé dans le classpath !");
            }

            Properties props = new Properties();
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            if (url == null || user == null || password == null) {
                throw new IllegalArgumentException("❌ Vérifie que 'db.url', 'db.user' et 'db.password' sont bien définis dans config.properties !");
            }

            // Chargement explicite du driver JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("✅ Connexion réussie à MySQL !");
            System.out.println("➡️ Utilisateur connecté : " + connection.getMetaData().getUserName());
            System.out.println("➡️ URL de connexion : " + connection.getMetaData().getURL());

        } catch (ClassNotFoundException e) {
            System.out.println("❌ Le driver JDBC MySQL est introuvable !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la connexion :");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Une erreur est survenue lors de la lecture du fichier de configuration ou de la connexion :");
            e.printStackTrace();
        }

        return connection;
    }
}
