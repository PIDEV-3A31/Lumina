package com.esprit.models;

public class user {
    private int id_user;
    private String username;
    private String password;

    public user(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public user(int id, String username, String password) {
        this.id_user = id;
        this.username = username;
        this.password = password;
    }

    public user(){};

    public int getId() {
        return id_user;
    }

    public void setId(int id) {
        this.id_user = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id_user +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
