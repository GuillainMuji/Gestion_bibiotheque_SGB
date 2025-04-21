package org.example;

import org.example.database.*;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final FichierMySQL fichierMySQL = new FichierMySQL();
    private static final Bibliotheque bibliotheque = new Bibliotheque();

    public static void main(String[] args) {


        Bibliotheque bibliotheque = new Bibliotheque(); // à placer avant la boucle si ce n'est pas déjà fait
        Bibliothecaire bibliothecaire = new Bibliothecaire(); // idem

        while (true) {
            System.out.println("\n=== SYSTÈME DE GESTION DE BIBLIOTHÈQUE ===");
            System.out.println("1. Se connecter (Admin)");
            System.out.println("2. Se connecter (Bibliothécaire)");
            System.out.println("3. Se connecter (Lecteur)");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option: ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // pour vider la ligne après le nextInt

            switch (choix) {
                case 1:
                    seConnecterAdmin();
                    break;
                case 2:
                    seConnecterBibliothecaire();
                    break;
                case 3:
                    seConnecterLecteur(bibliotheque, bibliothecaire);
                    break;
                case 4:
                    System.out.println("📕 Fermeture du programme...");
                    return;
                default:
                    System.out.println("❌ Choix invalide, veuillez réessayer.");
            }
        }
    }

    // Connexion ADMIN
    public static void seConnecterAdmin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        Utilisateur utilisateur = fichierMySQL.authentifierUtilisateur(email, password);
        if (utilisateur != null && "admin".equalsIgnoreCase(utilisateur.getRole())) {
            Admin admin = new Admin(utilisateur.getId(), utilisateur.getNom(), email, password, "admin");
            System.out.println("Connexion réussie en tant qu'Admin.");
            menuAdmin(admin);
        } else {
            System.out.println("Échec de la connexion. Vérifiez vos identifiants.");
        }
    }

    // Connexion BIBLIOTHÉCAIRE
    public static void seConnecterBibliothecaire() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        Utilisateur utilisateur = fichierMySQL.authentifierUtilisateur(email, password);
        if (utilisateur != null && "bibliothecaire".equalsIgnoreCase(utilisateur.getRole())) {
            Bibliothecaire bibliothecaire = new Bibliothecaire(utilisateur.getId(), utilisateur.getNom(), email, password, "bibliothecaire");
            System.out.println("Connexion réussie en tant que Bibliothécaire.");
            menuBibliothecaire(bibliotheque, bibliothecaire);
        } else {
            System.out.println("Échec de la connexion. Vérifiez vos identifiants.");
        }
    }

    // Connexion LECTEUR
    public static void seConnecterLecteur(Bibliotheque bibliotheque, Bibliothecaire bibliothecaire) {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        Utilisateur utilisateur = fichierMySQL.authentifierUtilisateur(email, password);
        if (utilisateur != null && "lecteur".equalsIgnoreCase(utilisateur.getRole())) {
            Lecteur lecteur = new Lecteur(utilisateur.getId(), utilisateur.getNom(), utilisateur.getEmail(), utilisateur.getPassword(), utilisateur.getRole());
            System.out.println("✅ Connexion réussie en tant que Lecteur.");

            // Appel du menu
            menuLecteur(fichierMySQL, lecteur);

        } else {
            System.out.println("❌ Échec de la connexion. Vérifiez vos identifiants.");
        }
    }
    // MENU ADMIN
    public static void menuAdmin(Admin admin) {
        int choix;

        do {
            System.out.println("\nMenu Admin:");
            System.out.println("1. Créer un bibliothécaire");
            System.out.println("2. Créer un lecteur");
            System.out.println("3. Lister tous les utilisateurs");
            System.out.println("4. Supprimer un utilisateur");
            System.out.println("5. Modifier le rôle d'un utilisateur");
            System.out.println("6. Déconnexion");
            System.out.print("Choisissez une option: ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    creerBibliothecaire(admin);
                    break;
                case 2:
                    creerLecteur(admin);
                    break;
                case 3:
                    listerUtilisateurs(admin);
                    break;
                case 4:
                    supprimerUtilisateur(admin);
                    break;
                case 5:
                    modifierRoleUtilisateur(admin);
                    break;
                case 6:
                    System.out.println("Déconnexion...");
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        } while (choix != 6);
    }

    public static void creerBibliothecaire(Admin admin) {
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        admin.creerBibliothecaire(bibliotheque, fichierMySQL, nom, email, password, "bibliothecaire");
    }

    public static void creerLecteur(Admin admin) {
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        admin.creerLecteur(bibliotheque, fichierMySQL, nom, email, password, "lecteur");
    }

    public static void listerUtilisateurs(Admin admin) {
        admin.listerUtilisateurs(bibliotheque, fichierMySQL);
    }

    public static void supprimerUtilisateur(Admin admin) {
        System.out.print("ID utilisateur: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        admin.supprimerUtilisateur(bibliotheque, fichierMySQL, id);
    }

    public static void modifierRoleUtilisateur(Admin admin) {
        System.out.print("ID utilisateur: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nouveau rôle: ");
        String role = scanner.nextLine();

        Utilisateur utilisateur = null;
        for (Utilisateur u : fichierMySQL.chargerUtilisateurs()) {
            if (u.getId() == id) {
                utilisateur = u;
                break;
            }
        }

        if (utilisateur != null) {
            admin.modifierRole(utilisateur, role);
            fichierMySQL.mettreAJourRole(id, role);
            System.out.println("Rôle mis à jour.");
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }


    private static void menuLecteur(FichierMySQL fichierMySQL, Lecteur lecteur) {
        while (true) {
            System.out.println("\n--- MENU LECTEUR ---");
            System.out.println("1. Emprunter un livre");
            System.out.println("2. Retourner un livre");
            System.out.println("3. Rechercher un livre");
            System.out.println("4. Lister les livres disponibles");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.print("Titre du livre à emprunter : ");
                    String titreEmp = scanner.nextLine();
                    Livre livreEmp = fichierMySQL.rechercherLivre(titreEmp);
                    if (livreEmp != null) {
                        if (livreEmp.isDisponible()) {
                            boolean success = fichierMySQL.emprunterLivre(livreEmp.getId(), lecteur.getId());
                            if (success) {
                                System.out.println("✅ Livre emprunté avec succès !");
                            } else {
                                System.out.println("❌ Échec de l’emprunt !");
                            }
                        } else {
                            System.out.println("❌ Livre non disponible !");
                        }
                    } else {
                        System.out.println("❌ Livre non trouvé !");
                    }
                    break;

                case 2:
                    System.out.print("Titre du livre à retourner : ");
                    String titreRet = scanner.nextLine();
                    Livre livreRet = fichierMySQL.rechercherLivre(titreRet);
                    if (livreRet != null) {
                        boolean success = fichierMySQL.retournerLivre(livreRet.getId());
                        if (success) {
                            System.out.println("✅ Livre retourné avec succès !");
                        } else {
                            System.out.println("❌ Échec du retour !");
                        }
                    } else {
                        System.out.println("❌ Livre non trouvé !");
                    }
                    break;

                case 3:
                    System.out.print("Titre du livre à rechercher : ");
                    String titreRecherche = scanner.nextLine();
                    Livre livre = fichierMySQL.rechercherLivre(titreRecherche);
                    if (livre != null) {
                        System.out.println("📘 Livre trouvé : " + livre);
                    } else {
                        System.out.println("❌ Livre non trouvé !");
                    }
                    break;

                case 4:
                    List<Livre> livres = fichierMySQL.listerLivresDisponibles();
                    if (livres.isEmpty()) {
                        System.out.println("📭 Aucun livre disponible.");
                    } else {
                        System.out.println("📚 Livres disponibles :");
                        for (Livre l : livres) {
                            System.out.println(l);
                        }
                    }
                    break;

                case 5:
                    return;

                default:
                    System.out.println("❌ Choix invalide, veuillez réessayer.");
            }
        }
    }
    // MENU BIBLIOTHÉCAIRE
    public static void menuBibliothecaire(Bibliotheque biblio, Bibliothecaire bibliothecaire) {
        while (true) {
            System.out.println("\n--- MENU BIBLIOTHÉCAIRE ---");
            System.out.println("1. Ajouter un livre");
            System.out.println("2. Supprimer un livre");
            System.out.println("3. Rechercher un livre");
            System.out.println("4. Lister les livres");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine();
            switch (choix) {
                case 1:
                    System.out.print("Titre : ");
                    String titre = scanner.nextLine();
                    System.out.print("Auteur : ");
                    String auteur = scanner.nextLine();
                    System.out.print("ISBN : ");
                    String isbn = scanner.nextLine();
                    System.out.print("Année de publication : ");
                    int annee = scanner.nextInt();
                    scanner.nextLine(); // vider le buffer
                    System.out.print("Catégorie : ");
                    boolean disponible = true;  // ou false, selon votre logique
                    String categorie = scanner.nextLine();
                    Livre livre = new Livre(0, titre, auteur, isbn, annee, categorie, disponible);
                    bibliothecaire.ajouterLivre(biblio, livre);
                    System.out.println("Livre ajouté avec succès !");
                    break;
                case 2:
                    System.out.print("Titre du livre à supprimer : ");
                    String titreSup = scanner.nextLine();
                    Livre livreASupprimer = bibliothecaire.rechercherLivre(biblio, titreSup);
                    if (livreASupprimer != null) {
                        bibliothecaire.supprimerLivre(biblio, livreASupprimer.getId());
                        System.out.println("Livre supprimé.");
                    } else {
                        System.out.println("Livre non trouvé !");
                    }
                    break;
                case 3:
                    System.out.print("Titre du livre à rechercher : ");
                    String titreRecherche = scanner.nextLine();
                    Livre livreRecherche = bibliothecaire.rechercherLivre(biblio, titreRecherche);
                    if (livreRecherche != null) {
                        System.out.println("Livre trouvé : " + livreRecherche);
                    } else {
                        System.out.println("Livre introuvable.");
                    }
                    break;
                case 4:
                    bibliothecaire.listerLivres(biblio);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}


/*package org.example;

import org.example.database.*;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final FichierMySQL fichierMySQL = new FichierMySQL();
    private static final Bibliotheque bibliotheque = new Bibliotheque();

    public static void main(String[] args) {
        int choix;

        while (true) {
            System.out.println("\n=== SYSTÈME DE GESTION DE BIBLIOTHÈQUE ===");
            System.out.println("1. Se connecter (Admin)");
            System.out.println("2. Se connecter (Bibliothécaire)");
            System.out.println("3. Se connecter (Lecteur)");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option: ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    seConnecterAdmin();
                    break;
                case 2:
                    seConnecterBibliothecaire();
                    break;
                case 3:
                    seConnecterLecteur();
                    break;
                case 4:
                    System.out.println("Fermeture du programme...");
                    return;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    // Connexion ADMIN
    public static void seConnecterAdmin() {
        System.out.print("Email: ");
        String nom = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        Utilisateur utilisateur = fichierMySQL.authentifierUtilisateur(nom, password);
        if (utilisateur != null && "admin".equalsIgnoreCase(utilisateur.getRole())) {
            Admin admin = new Admin(utilisateur.getId(), utilisateur.getNom(), nom, password, "admin");
            System.out.println("Connexion réussie en tant qu'Admin.");
            menuAdmin(admin);
        } else {
            System.out.println("Échec de la connexion. Vérifiez vos identifiants.");
        }
    }

    // Connexion BIBLIOTHÉCAIRE
    public static void seConnecterBibliothecaire() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        Utilisateur utilisateur = fichierMySQL.authentifierUtilisateur(email, password);
        if (utilisateur != null && "bibliothecaire".equalsIgnoreCase(utilisateur.getRole())) {
            Bibliothecaire bibliothecaire = new Bibliothecaire(utilisateur.getId(), utilisateur.getNom(), email, password, "bibliothecaire");
            System.out.println("Connexion réussie en tant que Bibliothécaire.");
            menuBibliothecaire(bibliotheque, bibliothecaire);
        } else {
            System.out.println("Échec de la connexion. Vérifiez vos identifiants.");
        }
    }

    // Connexion LECTEUR
    public static void seConnecterLecteur() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        Utilisateur utilisateur = fichierMySQL.authentifierUtilisateur(email, password);
        if (utilisateur != null && "lecteur".equalsIgnoreCase(utilisateur.getRole())) {
            Lecteur lecteur = new Lecteur(utilisateur.getId(), utilisateur.getNom(), email, password, "lecteur");
            System.out.println("Connexion réussie en tant que Lecteur.");
            lecteur.menuLecteur(bibliotheque); // Assurez-vous que cette méthode existe dans Lecteur
        } else {
            System.out.println("Échec de la connexion. Vérifiez vos identifiants.");
        }
    }

    // MENU ADMIN
    public static void menuAdmin(Admin admin) {
        int choix;

        do {
            System.out.println("\nMenu Admin:");
            System.out.println("1. Créer un bibliothécaire");
            System.out.println("2. Créer un lecteur");
            System.out.println("3. Lister tous les utilisateurs");
            System.out.println("4. Supprimer un utilisateur");
            System.out.println("5. Modifier le rôle d'un utilisateur");
            System.out.println("6. Déconnexion");
            System.out.print("Choisissez une option: ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    creerBibliothecaire(admin);
                    break;
                case 2:
                    creerLecteur(admin);
                    break;
                case 3:
                    listerUtilisateurs(admin);
                    break;
                case 4:
                    supprimerUtilisateur(admin);
                    break;
                case 5:
                    modifierRoleUtilisateur(admin);
                    break;
                case 6:
                    System.out.println("Déconnexion...");
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        } while (choix != 6);
    }

    public static void creerBibliothecaire(Admin admin) {
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        admin.creerBibliothecaire(bibliotheque, fichierMySQL, nom, email, password, "bibliothecaire");
    }

    public static void creerLecteur(Admin admin) {
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        admin.creerLecteur(bibliotheque, fichierMySQL, nom, email, password, "lecteur");
    }

    public static void listerUtilisateurs(Admin admin) {
        admin.listerUtilisateurs(bibliotheque, fichierMySQL);
    }

    public static void supprimerUtilisateur(Admin admin) {
        System.out.print("ID utilisateur: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        admin.supprimerUtilisateur(bibliotheque, fichierMySQL, id);
    }

    public static void modifierRoleUtilisateur(Admin admin) {
        System.out.print("ID utilisateur: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nouveau rôle: ");
        String role = scanner.nextLine();

        Utilisateur utilisateur = null;
        for (Utilisateur u : fichierMySQL.chargerUtilisateurs()) {
            if (u.getId() == id) {
                utilisateur = u;
                break;
            }
        }

        if (utilisateur != null) {
            admin.modifierRole(utilisateur, role);
            fichierMySQL.mettreAJourRole(id, role);
            System.out.println("Rôle mis à jour.");
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }

    // MENU BIBLIOTHÉCAIRE
    public static void menuBibliothecaire(Bibliotheque biblio, Bibliothecaire bibliothecaire) {
        while (true) {
            System.out.println("\n--- MENU BIBLIOTHÉCAIRE ---");
            System.out.println("1. Ajouter un livre");
            System.out.println("2. Supprimer un livre");
            System.out.println("3. Rechercher un livre");
            System.out.println("4. Lister les livres");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine();
            switch (choix) {
                case 1:
                    System.out.print("Titre : ");
                    String titre = scanner.nextLine();
                    System.out.print("Auteur : ");
                    String auteur = scanner.nextLine();
                    System.out.print("ISBN : ");
                    String isbn = scanner.nextLine();
                    System.out.print("Année de publication : ");
                    int annee = scanner.nextInt();
                    scanner.nextLine();
                    Livre livre = new Livre(0, titre, auteur, isbn, annee);
                    bibliothecaire.ajouterLivre(biblio, livre);
                    System.out.println("Livre ajouté avec succès !");
                    break;
                case 2:
                    System.out.print("Titre du livre à supprimer : ");
                    String titreSup = scanner.nextLine();
                    Livre livreASupprimer = bibliothecaire.rechercherLivre(biblio, titreSup);
                    if (livreASupprimer != null) {
                        bibliothecaire.supprimerLivre(biblio, livreASupprimer.getId());
                        System.out.println("Livre supprimé.");
                    } else {
                        System.out.println("Livre non trouvé !");
                    }
                    break;
                case 3:
                    System.out.print("Titre du livre à rechercher : ");
                    String titreRecherche = scanner.nextLine();
                    Livre livreRecherche = bibliothecaire.rechercherLivre(biblio, titreRecherche);
                    if (livreRecherche != null) {
                        System.out.println("Livre trouvé : " + livreRecherche);
                    } else {
                        System.out.println("Livre introuvable.");
                    }
                    break;
                case 4:
                    bibliothecaire.listerLivres(biblio);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}*/
