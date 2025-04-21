package org.example.database;

public class Livre {
    private int id;
    private String titre;
    private String auteur;
    private String isbn;
    private int annee;
    private boolean disponible;
    private String categorie;

    public Livre(int id, String titre, String auteur, String isbn, int annee, String categorie, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.annee = annee;
        this.disponible = disponible;
        this.categorie = categorie;
    }



    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public boolean isDisponible() {
        return disponible;
    }



    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Livre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", isbn='" + isbn + '\'' +
                ", annee=" + annee +
                ", disponible=" + disponible +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
