package org.example.database;

public class Utilisateur {
    private int id;
    private String nom;
    private String email;
    private String password;  // Le mot de passe de l'utilisateur
    private String role;      // Le rôle de l'utilisateur (ex. "admin", "utilisateur")

    // Constructeur


    public Utilisateur(int id, String nom, String email, String password, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Utilisateur() {

    }


    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Méthode pour afficher les informations de l'utilisateur (facultatif)
    @Override
    public String toString() {
        return "Utilisateur{id=" + id + ", nom='" + nom + "', email='" + email + "', role='" + role + "'}";
    }
}
