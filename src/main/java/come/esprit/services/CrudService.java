package come.esprit.services;

import come.esprit.models.Parking;

import java.util.List;


public interface CrudService <t>{
    void ajouter (t t);
    void modifier(t t);
    void supprimer(int id_park);
    List<t> afficher();  // Retourne une liste d'objets






}
