package com.esprit.models;

public class Point {
    private int pointId;
    private int userId;
    private String action;
    private int points;
    private String date;

    // Constructeur
    public Point(int userId,  int points, String action) {
        this.userId = userId;
        this.action = action;
        this.points = points;
        this.date = java.time.LocalDateTime.now().toString();
    }

    // Getters et setters
    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Méthode toString pour l'affichage
    @Override
    public String toString() {
        return "Point{" +
                "pointId=" + pointId +
                ", userId=" + userId +
                ", action='" + action + '\'' +
                ", points=" + points +
                ", date='" + date + '\'' +
                '}';
    }
}
