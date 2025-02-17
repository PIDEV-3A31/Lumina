package com.lumina.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

class News {
    private String title;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;



}


public class HttpClientExample {

    // Function to send a GET request and retrieve JSON data as a String



    public static String getJsonData(String url) {
        try {
            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a HttpRequest instance with the target URL
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Send the request and get the response as a String
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Return the body (JSON data) of the response
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        String url = "http://localhost:8000/";  // Sample API for JSON response
        //List<News> News = new List<News>();
        String jsonResponse = getJsonData(url);

        // all the json data needs to be in the list




        if (jsonResponse != null) {
            System.out.println(jsonResponse);
        } else {
            System.out.println("Failed to retrieve data.");
        }
    }
}
