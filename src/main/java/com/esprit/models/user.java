package com.esprit.models;

public class user {
    private int id_user;
    private String username;
    private String password;
    private String secret_Key;
    private boolean is_2fa_enabled;

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

    public user(int idUser, String username, String password, boolean is2faEnabled, String secretKey) {
        this.id_user = idUser;
        this.username = username;
        this.password = password;
        this.is_2fa_enabled = is2faEnabled;
        this.secret_Key = secretKey;
    }

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

    public String getSecret_Key() {
        return secret_Key;
    }

    public void setSecret_Key(String secret_Key) {
        this.secret_Key = secret_Key;
    }

    public boolean isIs_2fa_enabled() {
        return is_2fa_enabled;
    }

    public void setIs_2fa_enabled(boolean is_2fa_enabled) {
        this.is_2fa_enabled = is_2fa_enabled;
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
