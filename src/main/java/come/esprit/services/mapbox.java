package come.esprit.services;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;



public class mapbox extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Création du WebView
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Code HTML avec Mapbox JS
        String htmlContent = "<html>" +
                "<head>" +
                "<script src='https://api.mapbox.com/mapbox-gl-js/v2.3.1/mapbox-gl.js'></script>" +
                "<link href='https://api.mapbox.com/mapbox-gl-js/v2.3.1/mapbox-gl.css' rel='stylesheet' />" +
                "</head>" +
                "<body style='margin:0; padding:0;'>" +
                "<div id='map' style='width: 100%; height: 100vh;'></div>" +
                "<script>" +
                "mapboxgl.accessToken = 'YOUR_MAPBOX_ACCESS_TOKEN';" +
                "var map = new mapboxgl.Map({" +
                "container: 'map'," +
                "style: 'mapbox://styles/mapbox/streets-v11'," +
                "center: [0, 0], // Longitude, Latitude" +
                "zoom: 2" +
                "});" +
                "</script>" +
                "</body>" +
                "</html>";

        // Charger le code HTML dans le WebView
        webEngine.loadContent(htmlContent);

        // Ajouter le WebView à la scène
        StackPane root = new StackPane();
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Mapbox Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
