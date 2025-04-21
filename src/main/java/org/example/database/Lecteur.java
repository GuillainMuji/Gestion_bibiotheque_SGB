package org.example.database;

import java.util.Scanner;

public class Lecteur extends Utilisateur {
    private static final Scanner scanner = new Scanner(System.in);

    public Lecteur(int id, String nom, String email, String password, String role) {
        super(id, nom, email, password, role);
    }

    // Emprunter un livre
    public void emprunterLivre(Livre livre) {
        if (livre.isDisponible()) {
            livre.setDisponible(false);
            System.out.println("📕 Livre emprunté : " + livre.getTitre());
        } else {
            System.out.println("Le livre " + livre.getTitre() + " est déjà emprunté.");
        }
    }

    // Retourner un livre
    public void retournerLivre(Livre livre) {
        livre.setDisponible(true);
        System.out.println("📗 Livre retourné : " + livre.getTitre());
    }



    // Lister les livres
    private void listerLivres(Bibliotheque biblio) {
        if (biblio.getLivres().isEmpty()) {
            System.out.println("Aucun livre disponible.");
        } else {
            System.out.println("\n📚 Livres disponibles :");
            for (Livre livre : biblio.getLivres()) {
                if (livre.isDisponible()) {
                    System.out.println(livre);
                }
            }
        }
    }

    // Rechercher un livre
    private void rechercherLivre(Bibliotheque biblio) {
        System.out.print("Titre du livre : ");
        String titre = scanner.nextLine();
        boolean trouve = false;
        for (Livre livre : biblio.getLivres()) {
            if (livre.getTitre().equalsIgnoreCase(titre)) {
                System.out.println("📘 Livre trouvé : " + livre);
                trouve = true;
                break;
            }
        }
        if (!trouve) {
            System.out.println("Aucun livre trouvé avec ce titre.");
        }
    }

    // toString
    @Override
    public String toString() {
        return "Lecteur{id=" + this.getId() + ", nom='" + this.getNom() + "', password='" + this.getPassword() + "', role='" + this.getRole() + "'}";
    }
}
