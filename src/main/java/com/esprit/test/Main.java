package com.esprit.test;

import com.esprit.models.Demandes;
import com.esprit.models.Documents;
import com.esprit.models.KYC;
import com.esprit.services.ServiceDemande;
import com.esprit.services.ServiceDocuments;
import com.esprit.utils.DataBase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import static com.esprit.models.KYC.*;

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

/*
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
        System.out.println("***********************************");
        ServiceDemande s2 = new ServiceDemande();
        s2.ajouter(new Demandes(1, 1, "extrait",new Date(), "En attente","des"));
        s2.modifier(new Demandes(2,1, 1, "extrait",new Date(), "En cours","desdesdes"));
        s2.supprimer(4);
        for (Demandes d : s2.consulter()) {
            System.out.println("ID: " + d.getId_demande() +
                    " | Utilisateur: " + d.getId_utilisateur() +
                    " | Document: " + d.getId_document() +
                    " | Date: " + d.getDate_demande() +
                    " | Statut: " + d.getStatut_demande());
        }

        //s2.supprimer(3);

*/
/*
        try {
            KYC.createApplicant();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        String filePath = "C:\\Users\\syrin\\Desktop\\Projet Web Java\\Tunisia-Passport.png";
        String responseString = uploadDocument("30dfa25d-a536-4c96-a674-33aea1cf0e85", filePath);
        System.out.println("response : "+responseString);
        String checkId = extractCheckId(responseString);
        if (checkId != null) {
            System.out.println("Check ID: " + checkId);

            // Step 4: Check the status of the verification
            checkVerificationStatus(checkId);
        } else {
            System.out.println("No check_id found in the response.");
        }
*/
/*

        try {
            // Step 1: Create an applicant
            String applicantId = createApplicant();  // Modify to return the applicant ID
            if (applicantId != null) {
                System.out.println("Applicant created with ID: " + applicantId);

                // Step 2: Upload a document for verification
                String filePath = "C:/Users/syrin/Desktop/Projet Web Java/Tunisia-Passport.png";
                File file = new File(filePath);
                if (!file.exists()) {  // ✅ Correct, file est un objet de type File
                    System.out.println("⚠️ Erreur : Le fichier '" + filePath + "' n'existe pas !");
                } else {
                    System.out.println("✅ Le fichier existe : " + filePath);
                }

                String documentId = uploadDocument(applicantId, filePath);  // Upload the document
                if (documentId != null) {
                    System.out.println("Document uploaded successfully with ID: " + documentId);

                    // Step 3: Create a check for the applicant using the document ID
                    String checkId = createCheck(applicantId, documentId);  // Pass the documentId to create check
                    if (checkId != null) {
                        System.out.println("Check ID: " + checkId);

                        // Step 4: Check the status of the verification
                        checkVerificationStatus(checkId);
                    } else {
                        System.out.println("Check creation failed.");
                    }
                } else {
                    System.out.println("Document upload failed.");
                }
            } else {
                System.out.println("Applicant creation failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

/*
        try {
            // Step 1: Create an applicant with the breakdown combination
            String applicantId = createApplicant("Image Integrity - Supported Document", "Smith");
            if (applicantId != null) {
                System.out.println("Applicant created with ID: " + applicantId);

                // Step 2: Upload a document for verification
                String filePath = "C:/Users/syrin/Desktop/Projet Web Java/Tunisia-Passport.png";
                BufferedImage image = ImageIO.read(new File("C:/Users/syrin/Desktop/Projet Web Java/Tunisia-Passport.png"));
                ImageIO.write(image, "jpg", new File("C:/Users/syrin/Desktop/Projet Web Java/Tunisia-Passport.jpg"));

                File file = new File(filePath);
                if (!file.exists()) {
                    System.err.println("Error: File does not exist at path: " + filePath);
                    return;
                }

                String documentId = uploadDocument(applicantId, filePath);
                if (documentId != null) {
                    System.out.println("Document uploaded successfully with ID: " + documentId);

                    // Step 3: Create a check for the applicant using the document ID
                    String checkId = createCheck(applicantId, documentId);
                    if (checkId != null) {
                        System.out.println("Check ID: " + checkId);

                        // Step 4: Check the status of the verification
                        checkVerificationStatus(checkId);
                    } else {
                        System.out.println("Check creation failed.");
                    }
                } else {
                    System.out.println("Document upload failed.");
                }
            } else {
                System.out.println("Applicant creation failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
}

