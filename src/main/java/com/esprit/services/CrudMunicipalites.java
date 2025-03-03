package com.esprit.services;

import java.util.List;

public interface CrudMunicipalites<T> {
    void ajouter(T obj);
    void modifier(T obj);
    void supprimer(int id);
    List<T> consulter();
}
