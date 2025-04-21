package org.example.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bibliotheque {
    private List<Utilisateur> utilisateurs = new ArrayList();
    private List<Livre> livres = new ArrayList();
    private List<Emprunt> emprunts = new ArrayList();

    public Bibliotheque() {
    }

    public List<Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    public List<Livre> getLivres() {
        return this.livres;
    }

    public List<Emprunt> getEmprunts() {
        return this.emprunts;
    }

    public void ajouterEmprunt(Emprunt emprunt) {
        this.emprunts.add(emprunt);
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public void setLivres(List<Livre> livres) {
        this.livres = livres;
    }

    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }

    private List<Livre> listeLivres = new ArrayList<>();

    public void ajouterLivre(Livre livre) {
        listeLivres.add(livre);
        System.out.println("📚 Livre ajouté à la bibliothèque locale : " + livre.getTitre());
    }
}
