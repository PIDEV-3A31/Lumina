package com.esprit.test;

import com.esprit.models.Demandes;
import com.esprit.models.Documents;
import com.esprit.services.ServiceDemande;
import com.esprit.services.ServiceDocuments;
import com.esprit.utils.DataBase;

import java.util.Date;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DataBase db = new DataBase();
        ServiceDocuments sd = new ServiceDocuments();
        ServiceDocuments sd1 = new ServiceDocuments();
        /*sd.ajouter(new Documents(
               "PDF",
               "Rapport Financier",
                "Rapport annuel de l'année",
                new Date(),
                new Date(),
                "/documents/rapport.pdf"
        ));

        sd1.ajouter(new Documents(
                "DOCX",
                "Plan d'urbanisme",
                "Document décrivant le plan d'urbanisme de la municipalité",
                new Date(),
                new Date(),
                "/municipalite/plan_urbanisme.pdf"
        ));

         */
        /*sd1.modifier(new Documents(
                2,
                "PDF",
                "Plan d'urbanisme",
                "Document décrivant le plan d'urbanisme de la municipalité",
                new Date(),
                new Date(),
                "/municipalite/plan_urbanisme.pdf"
        ));
        */
        //sd1.supprimer(3);
        List<Documents> documents = sd.consulter();

        if (documents.isEmpty()) {
            System.out.println("Aucun document trouvé !");
        } else {
            for (Documents d : documents) {
                System.out.println(" ID: " + d.getId_document() +
                        " | Type: " + d.getType_document() +
                        " | Titre: " + d.getTitre() +
                        " | Description: " + d.getDescription() +
                        " | Créé le: " + d.getDate_creation() +
                        " | Modifié le: " + d.getDate_modification() +
                        " | Fichier: " + d.getChemin_fichier());
            }
        }

        ServiceDemande s2 = new ServiceDemande();
        //s2.ajouter(new Demandes(1, 1, new Date(), "En attente"));
        //s2.modifier(new Demandes(2,1, 1, new Date(), "En cours"));
        //s2.supprimer(4);
        for (Demandes d : s2.consulter()) {
            System.out.println("ID: " + d.getId_demande() +
                    " | Utilisateur: " + d.getId_utilisateur() +
                    " | Document: " + d.getId_document() +
                    " | Date: " + d.getDate_demande() +
                    " | Statut: " + d.getStatut_demande());
        }

    }
}