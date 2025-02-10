package com.esprit;
import com.esprit.models.ligneTransport;
import com.esprit.models.moyenTransport;
import com.esprit.models.reservation;
import com.esprit.services.serviceLigneTransport;
import com.esprit.services.serviceMoyenTransport;
import com.esprit.services.serviceReservation;
import com.esprit.utils.DataBase;

import java.sql.Time;
import java.util.Date;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        serviceLigneTransport slt = new serviceLigneTransport();
        serviceMoyenTransport smt = new serviceMoyenTransport();
        serviceReservation sr = new serviceReservation();

       /* slt.ajouter(new ligneTransport(
                "Ligne C",
                "tunis",
                5.00,
                Time.valueOf("18:00:00"),
                Time.valueOf("15:30:00"),
                "Actif"
        ));

        slt.modifier(new ligneTransport(
                1,
                "Ligne A ",
                "Centre-ville ",
                6.50,
                Time.valueOf("09:00:00"),
                Time.valueOf("16:00:00"),
                "Inactif"
        ));*/

        System.out.println("\n=== LISTE DES LIGNE DES TRANSPORT ===");
        List<ligneTransport> lignes = slt.consulter();
        for (ligneTransport ligne : lignes) {
            System.out.println(ligne);
        }

        //smt.ajouter(new moyenTransport( 1, "train", 250, "456-XYZ", "Actif"));
        //smt.modifier(new moyenTransport(1, 1, "Minibus", 30, "789-ABC", "Maintenance"));
        //smt.supprimer(2);

        System.out.println("\n=== LISTE DES MOYENS DE TRANSPORT ===");
        List<moyenTransport> moyens = smt.consulter();
        for (moyenTransport moyen : moyens) {
            System.out.println(moyen);
        }


        //sr.ajouter(new reservation(1, 1, new Date(), 5, 15.00, "Confirmée"));
        //sr.ajouter(new reservation(2, 1, new Date(), 3, 10.00, "En attente"));


        //sr.modifier(new reservation(4, 1, 1, new Date(), 4, 20.00, "Annulée"));
        //sr.supprimer(2);

        System.out.println("\n=== LISTE DES RÉSERVATIONS ===");
        List<reservation> reservations = sr.consulter();
        for (reservation r : reservations) {
            System.out.println(r);
        }

    }
}