
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.esprit.services;

import java.util.List;

public interface CrudService<T> {
    void ajouter(T var1);

    void modifer(T var1,int var2);

    void supprimer(int var1);

    List<T> afficher();
}
