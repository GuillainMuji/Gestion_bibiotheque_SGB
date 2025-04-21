package org.example.database;

import java.util.Iterator;
import java.util.List;

public class Admin extends Utilisateur {
    public Admin(int id, String nom, String email, String password, String role) {
        super(id, nom, email, password, role);
    }

    // Créer un bibliothécaire et l'enregistrer dans la base + mémoire
    public void creerBibliothecaire(Bibliotheque biblio, FichierMySQL fichierMySQL, String nom, String email, String password, String role) {
        // Générer un ID (ou laisser MySQL auto-incrémenter si géré côté DB)
        int id = fichierMySQL.genererNouvelId(); // tu peux l'implémenter ou remplacer par -1 si auto-incrémenté

        Bibliothecaire bibliothecaire = new Bibliothecaire(id, nom, email, password, role);
        biblio.getUtilisateurs().add(bibliothecaire); // en mémoire
        fichierMySQL.ajouterUtilisateur(bibliothecaire); // en base
        System.out.println("Bibliothécaire créé : " + bibliothecaire);
    }

    // Créer un lecteur et l'enregistrer dans la base + mémoire
    public void creerLecteur(Bibliotheque biblio, FichierMySQL fichierMySQL, String nom, String email, String password, String role) {
        int id = fichierMySQL.genererNouvelId();

        Lecteur lecteur = new Lecteur(id, nom, email, password, role);
        biblio.getUtilisateurs().add(lecteur);
        fichierMySQL.ajouterUtilisateur(lecteur);
        System.out.println("Lecteur créé : " + lecteur);
    }

    // Lister les utilisateurs (depuis la base si tu veux être à jour)
    public void listerUtilisateurs(Bibliotheque bibliotheque, FichierMySQL fichierMySQL) {
        List<Utilisateur> utilisateurs = fichierMySQL.chargerUtilisateurs();
        if (utilisateurs.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
        } else {
            for (Utilisateur u : utilisateurs) {
                System.out.println("ID: " + u.getId() + ", Nom: " + u.getNom() +
                        ", Email: " + u.getEmail() + ", Rôle: " + u.getRole());
            }
        }
    }

    public void supprimerUtilisateur(Bibliotheque biblio, FichierMySQL fichierMySQL, int utilisateurId) {
        Iterator<Utilisateur> iterator = biblio.getUtilisateurs().iterator();
        while (iterator.hasNext()) {
            Utilisateur utilisateur = iterator.next();
            if (utilisateur.getId() == utilisateurId) {
                iterator.remove();
                fichierMySQL.supprimerUtilisateur(utilisateurId); // supprime aussi en base
                System.out.println("Utilisateur supprimé : " + utilisateur);
                return;
            }
        }
        System.out.println("Utilisateur non trouvé avec l'ID : " + utilisateurId);
    }

    public void modifierRole(Utilisateur utilisateur, String nouveauRole) {
        utilisateur.setRole(nouveauRole);
        System.out.println("Rôle de l'utilisateur " + utilisateur.getNom() + " modifié en : " + nouveauRole);
        // Si tu veux mettre à jour en base aussi :
        // fichierMySQL.mettreAJourRole(utilisateur.getId(), nouveauRole); (à implémenter)
    }
}
