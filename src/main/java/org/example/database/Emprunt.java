package org.example.database;

import java.time.LocalDate;

public class Emprunt {
    private int utilisateurId;
    private int livreId;
    private LocalDate dateEmprunt;
    private LocalDate dateRetour;

    // Constructeur correct
    public Emprunt(int utilisateurId, int livreId, LocalDate dateEmprunt, LocalDate dateRetour) {
        this.utilisateurId = utilisateurId;
        this.livreId = livreId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }

    // Getters
    public int getUtilisateurId() {
        return utilisateurId;
    }

    public int getLivreId() {
        return livreId;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    // Setters
    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }

    // toString
    @Override
    public String toString() {
        return "Emprunt{" +
                "utilisateurId=" + utilisateurId +
                ", livreId=" + livreId +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetour=" + dateRetour +
                '}';
    }
}
