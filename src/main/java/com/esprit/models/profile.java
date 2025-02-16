package com.esprit.models;

import java.sql.Timestamp;

public class profile {
    private int id_profile;
    private int id_user,phone_u;
    private String name_u,email_u,role,image_u;
    private Timestamp updated_at,created_at;

    public profile(int idUser, int idProfile, String name_u, String email_u, int phone, String role, String image_u) {
        this.id_user = idUser;
        this.id_profile = idProfile;
        this.name_u = name_u;
        this.email_u = email_u;
        this.phone_u = phone;
        this.role = role;
        this.image_u = image_u;
    }

    public String getImage_u() {
        return image_u;
    }

    public void setImage_u(String image_u) {
        this.image_u = image_u;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public profile(int id_user, String name_u, String email_u, int phone_u, String role) {
        this.id_user = id_user;
        this.name_u = name_u;
        this.email_u = email_u;
        this.phone_u = phone_u;
        this.role = role;
    }
    public profile(int id_user, String name_u, String email_u, int phone_u, String role, String image) {
        this.id_user = id_user;
        this.name_u = name_u;
        this.email_u = email_u;
        this.phone_u = phone_u;
        this.role = role;
        this.image_u = image;
    }
    public profile(int id_user, int id_profile, String name_u, String email_u,int phone_u, String role) {
        this.id_user = id_user;
        this.id_profile = id_profile;
        this.name_u = name_u;
        this.email_u = email_u;
        this.phone_u = phone_u;
        this.role = role;
    }
    public profile(String name_u, String email_u, int phone_u, String role) {
        this.name_u = name_u;
        this.email_u = email_u;
        this.phone_u = phone_u;
        this.role = role;
    }
    public profile(String name_u, String email_u, int phone_u, String role, Timestamp t) {
        this.name_u = name_u;
        this.email_u = email_u;
        this.phone_u = phone_u;
        this.role = role;
        this.updated_at = t;
    }

    public profile() {};

    public int getId_profile() {
        return id_profile;
    }

    public void setId_profile(int id_profile) {
        this.id_profile = id_profile;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getEmail_u() {
        return email_u;
    }

    public void setEmail_u(String email_u) {
        this.email_u = email_u;
    }

    public String getName_u() {
        return name_u;
    }

    public void setName_u(String name_u) {
        this.name_u = name_u;
    }

    public int getPhone_u() {
        return phone_u;
    }

    public void setPhone_u(int phone_u) {
        this.phone_u = phone_u;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "profile{" +
                "id_profile=" + id_profile +
                ", id_user=" + id_user +
                ", name_u='" + name_u + '\'' +
                ", email_u='" + email_u + '\'' +
                ", phone_u='" + phone_u + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
