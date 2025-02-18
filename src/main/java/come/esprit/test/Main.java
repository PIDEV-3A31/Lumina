package come.esprit.test;
import java.util.List;
import come.esprit.models.Reservation;
import come.esprit.services.ServiceReservation;
import come.esprit.models.Parking;
import come.esprit.services.ServiceParking;


// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


        ServiceParking serviceParking = new ServiceParking();
       //serviceParking.ajouter(new Parking("nour", 160, "place_disponibles", "Bardo", "2DT/H",19));


         Parking p = new Parking("radesss", 600 , "place_disponibles", "Ariana", "6DT/H",600);
        p.setId_parck(16);  // Définir l'ID du parking à modifier
       // serviceParking.modifier(p);  // Appeler la méthode pour modifier ce parking


        // serviceParking.supprimer(15);


         List<Parking> listParkings = serviceParking.afficher();
         for (Parking pp : listParkings) {
           System.out.println("ID: " + pp.getId_parck() + ", Nom: " + pp.getName_parck() + ", Capacity: " + pp.getCapacity() + ", status_parking: " + pp.getStatus_parking() +", adresses: "+pp.getAdresses() +"tarif: "+pp.getTarif() +"places_reservees: "+ pp.getPlaces_reservees());
        }




         //reservation
        ServiceReservation ServiceReservation = new ServiceReservation();


       Reservation reservation1 = new Reservation();
        reservation1.setId_parck(26);  // Assure-toi qu'il y a un parking avec id 1
        //  reservation1.setDate_reservation(java.sql.Date.valueOf("2025-02-12"));  // Exemple de date
        reservation1.setMatricule_voiture("111 tn 1920");

      //ServiceReservation.ajouter(reservation1);

       // reservation1.setId_reservation(3);  // ID de la réservation à modifier
       // reservation1.setId_parck(15);  // ID du parking associé à la réservation
        //reservation1.setDate_reservation(new java.util.Date());  // Nouvelle date de réservation
      // reservation1.setMatricule_voiture("173 tn 1000");  // Matricule de la voiture de la réservation

// Appel de la méthode pour modifier la réservation
        // ServiceReservation.modifier(reservation1);


        // ID de la réservation à supprimer
        int idReservation = 2;  // Remplacer par l'ID de la réservation à supprimer

// Appel de la méthode pour supprimer la réservation
        //ServiceReservation.supprimer(idReservation);  // Supprimer la réservation dans la base de données


       List<Reservation> reservations = ServiceReservation.afficher();  // Afficher toutes les réservations

 //Affichage de chaque réservation
        for (Reservation reservation : reservations) {
            System.out.println(reservation.toString());  // Afficher les détails de chaque réservation
        }


    }
    }
