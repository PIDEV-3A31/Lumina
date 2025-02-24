package com.esprit;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceProfile;
import com.esprit.services.ServiceUser;
import com.esprit.utils.PasswordEncryption;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*ServiceUser su = new ServiceUser();
        user u1 = new user("nour.allegui","1234");
        user u2 = new user("test12","2587");
       su.ajouter(u2);
       // su.modifer(u1,2);
       // su.supprimer(3);
        ServiceProfile sp = new ServiceProfile();
        List<user> users = su.afficher();
        profile p = new profile(2,"nour","nour@esprit.tn",98568475,"admin");
        profile p2 = new profile(4,"test","test@esprit.tn",27732307,"user");
        sp.ajouter(p2);
       // sp.supprimer(5);
        List<profile> profils = sp.afficher();
       // sp.modifer(p2,3);
        System.out.println(profils);
        // Afficher les utilisateurs
       for (user user : users) {
            System.out.println("Username: " + user.getUsername() + ", Password: " + user.getPassword());
        }




        //sp.ajouter(p);*/
        try {

                    String password = PasswordEncryption.encryptPass("N12");
            System.out.println(password);

                } catch (Exception e) {
                    e.printStackTrace();
                }




    }
}