package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceProfile;
import com.esprit.services.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.SimpleObjectProperty;
import com.esprit.utils.NavigationHistory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;
/*import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;*/
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.stage.FileChooser;

import javax.swing.text.Document;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class dashboardController {
    private user connectedUser;
    private profile userProfile;

    @FXML
    private Label name_current_user;

    @FXML
    private TableView<profile> tableView;
    @FXML
    private TableColumn<profile, String> columnUsername;
    @FXML
    private TableColumn<profile, String> columnRole;

    // Labels pour les détails de l'utilisateur sélectionné
    @FXML
    private Label username_userselectionne;
    @FXML
    private Label name_userselectionne;
    @FXML
    private Label email_userselectionne;
    @FXML
    private Label phone_userselectionne;
    @FXML
    private Label role_userselectionne;
    @FXML
    private Label createdat_userselectionne;
    @FXML
    private Label updatedat_userselectionne;
    @FXML
    private TableColumn<profile, ImageView> columnImage;

    @FXML
    private Button suppBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button ajouter_user;

    @FXML
    private ImageView img_current_user;
    @FXML
    private ImageView image_userselectionne;

    @FXML
    private Button deconnexion;

    @FXML
    private TextField search_field;

    @FXML
    private Button ExportPdf;

    @FXML
    private ImageView QrCode;

    @FXML
    public void initialize() {
        columnUsername.setCellValueFactory(cellData -> {
            ServiceUser serviceUser = new ServiceUser();
            user u = serviceUser.getUserById(cellData.getValue().getId_user());
            return new SimpleStringProperty(u.getUsername());
        });

        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Configurer la colonne d'image une seule fois
        columnImage.setCellValueFactory(param -> {
            profile p = param.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);

            if (p.getImage_u() != null) {
                try {
                    Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + p.getImage_u())).toExternalForm());
                    imageView.setImage(img);
                } catch (Exception e) {
                    System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
                }
            }

            return new SimpleObjectProperty<>(imageView);
        });
        loadTableData();
        // Ajouter un listener pour la sélection dans la TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showUserDetails(newSelection);
            }
        });

        suppBtn.setOnAction(event -> Delete());

        editBtn.setOnAction(event -> Edit());

        ajouter_user.setOnAction(event -> Add());

        name_current_user.setOnMouseClicked(event -> editCurrentUserProfile());

        name_current_user.setStyle("-fx-cursor: hand;");

        deconnexion.setOnAction(event -> logout());

        initializeSearch();

        //ExportPdf.setOnAction(event -> exportToPDF());
    }

    public void initData(user user, profile profile) {
        this.connectedUser = user;
        this.userProfile = profile;
        updateUI();
        loadTableData();
    }

    private void updateUI() {
        if (userProfile != null) {
            name_current_user.setText(userProfile.getName_u());

            if (userProfile.getImage_u() != null) {
                try {
                    Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + userProfile.getImage_u())).toExternalForm());
                    img_current_user.setImage(img);
                } catch (Exception e) {
                    System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
                }
            }
        }
    }

    private void loadTableData() {
        ServiceProfile serviceProfile = new ServiceProfile();
        ObservableList<profile> profiles = FXCollections.observableArrayList(serviceProfile.afficher());

        // Configuration des colonnes
        columnUsername.setCellValueFactory(cellData -> {
            ServiceUser serviceUser = new ServiceUser();
            user u = serviceUser.getUserById(cellData.getValue().getId_user());
            return new SimpleStringProperty(u.getUsername());
        });

        columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Configuration de la colonne d'image
        columnImage.setCellValueFactory(param -> {
            profile p = param.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);

            if (p.getImage_u() != null) {
                try {
                    Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + p.getImage_u())).toExternalForm());
                    imageView.setImage(img);
                } catch (Exception e) {
                    System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
                }
            }

            return new SimpleObjectProperty<>(imageView);
        });

        tableView.setItems(profiles);
        initializeSearch(); // Réinitialiser la recherche après le chargement des données
    }

    private void showUserDetails(profile selectedProfile) {
        try {
            ServiceUser serviceUser = new ServiceUser();
            user selectedUser = serviceUser.getUserById(selectedProfile.getId_user());
            
            // Afficher les détails existants
            username_userselectionne.setText(selectedUser.getUsername());
            name_userselectionne.setText(selectedProfile.getName_u());
            email_userselectionne.setText(selectedProfile.getEmail_u());
            phone_userselectionne.setText(String.valueOf(selectedProfile.getPhone_u()));
            role_userselectionne.setText(selectedProfile.getRole());
            if (selectedProfile.getCreated_at() != null) {
                createdat_userselectionne.setText(selectedProfile.getCreated_at().toString());
            }
            if (selectedProfile.getUpdated_at() != null) {
                updatedat_userselectionne.setText(selectedProfile.getUpdated_at().toString());
            }

            // Générer et afficher le QR Code
            generateQRCode(selectedProfile.getEmail_u());

            // Charger l'image du profil
            loadImage(selectedProfile.getImage_u(), image_userselectionne);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading user details: " + e.getMessage());
        }
    }

    private void generateQRCode(String email) {
        try {
            // Créer l'URL mailto
            String mailtoUrl = "mailto:" + email;

            // Encoder l'URL pour l'API GoQR
            String encodedUrl = URLEncoder.encode(mailtoUrl, "UTF-8");

            // Construire l'URL de l'API GoQR
            String qrCodeApiUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + encodedUrl;

            // Créer la connexion
            URL url = new URL(qrCodeApiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Récupérer l'image du QR code
            try (InputStream inputStream = connection.getInputStream()) {
                Image qrCodeImage = new Image(inputStream);
                QrCode.setImage(qrCodeImage);
                
                // Ajouter un effet de survol pour indiquer qu'il est cliquable
                QrCode.setStyle("-fx-cursor: hand;");
                
                // Ajouter un gestionnaire de clic pour ouvrir le client de messagerie
                QrCode.setOnMouseClicked(event -> {
                    try {
                        java.awt.Desktop.getDesktop().mail(new java.net.URI("mailto:" + email));
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", 
                            "Could not open email client: " + e.getMessage());
                    }
                });
            }

            // Fermer la connexion
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Error generating QR code: " + e.getMessage());
        }
    }

    private void Delete() {
        profile selectedProfile = tableView.getSelectionModel().getSelectedItem();
        if (selectedProfile == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un utilisateur à supprimer!");
            return;
        }

        // Demander confirmation avant la suppression
        Optional<ButtonType> result = showConfirmation("Confirmation",
            "Êtes-vous sûr de vouloir supprimer cet utilisateur ?",
            "Cette action ne peut pas être annulée.");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ServiceUser serviceUser = new ServiceUser();
                serviceUser.supprimer(selectedProfile.getId_user());

                loadTableData();

                clearUserDetails();

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur supprimé avec succès!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la suppression : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Optional<ButtonType> showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearUserDetails() {
        username_userselectionne.setText("");
        name_userselectionne.setText("");
        email_userselectionne.setText("");
        phone_userselectionne.setText("");
        role_userselectionne.setText("");
        createdat_userselectionne.setText("");
        updatedat_userselectionne.setText("");
    }

    private void Edit() {
        profile selectedProfile = tableView.getSelectionModel().getSelectedItem();
        if (selectedProfile == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un utilisateur à modifier!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardModifProf.fxml"));
            System.out.println(getClass().getResource("/dashboardModifProf.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et initialiser les données
            dashboardModifProf modifController = loader.getController();
            modifController.initData(connectedUser, userProfile, selectedProfile);

            Stage stage = (Stage) editBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }

    private void Add() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardModifProf.fxml"));
            Parent root = loader.load();

            dashboardModifProf modifController = loader.getController();
            modifController.initDataForAdd(connectedUser, userProfile);

            Stage stage = (Stage) ajouter_user.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }

    private void editCurrentUserProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboardAffichProf.fxml"));
            Parent root = loader.load();

            dashboardAffichProf controller = loader.getController();
            controller.initData(connectedUser, userProfile);

            Stage stage = (Stage) name_current_user.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }

    /*private void navigateBack() {
        try {
            String previousPage = NavigationHistory.popPage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(previousPage));
            Parent root = loader.load();

            // Initialiser le contrôleur selon la page
            Object controller = loader.getController();
            if (controller instanceof dashboardController) {
                ((dashboardController) controller).initData(connectedUser, userProfile);
            } else if (controller instanceof dashboardAffichProf) {
                ((dashboardAffichProf) controller).initData(connectedUser, userProfile);
            }
            // ... autres cas selon vos contrôleurs

            Stage stage = (Stage) retourBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void logout() {
        Stage currentStage = (Stage) deconnexion.getScene().getWindow();
        loginn.logout(currentStage);
    }

    public void refreshTableView() {
        ServiceProfile serviceProfile = new ServiceProfile();
        ObservableList<profile> profiles = FXCollections.observableArrayList(serviceProfile.afficher());
        tableView.setItems(profiles);
        tableView.refresh();

        // Forcer le rafraîchissement de chaque cellule
        for (profile p : profiles) {
            if (p.getImage_u() != null) {
                System.out.println("Refreshing image for profile " + p.getId_profile() + ": " + p.getImage_u());
            }
        }
    }

    private void loadImage(String imagePath, ImageView imageView) {
        if (imagePath != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + imagePath)).toExternalForm());
                imageView.setImage(img);
                System.out.println(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: du loadimg " + e.getMessage());
            }
        }
    }

    private void UserSelection(profile selectedProfile) {
        if (selectedProfile != null) {
            // ... code existant ...
            loadImage(selectedProfile.getImage_u(), image_userselectionne);
        }
    }

    private void initializeSearch() {
        // Obtenir la liste complète des profils
        ServiceProfile serviceProfile = new ServiceProfile();
        ObservableList<profile> profileList = FXCollections.observableArrayList(serviceProfile.afficher());

        // Créer une FilteredList wrappant la ObservableList
        FilteredList<profile> filteredData = new FilteredList<>(profileList, p -> true);

        // Ajouter un listener au champ de recherche
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(profile -> {
                // Si le champ de recherche est vide, afficher tous les profils
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Comparer avec le username
                ServiceUser serviceUser = new ServiceUser();
                user u = serviceUser.getUserById(profile.getId_user());
                if (u.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        // Wrap la FilteredList dans une SortedList
        SortedList<profile> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Ajouter les données filtrées et triées à la TableView
        tableView.setItems(sortedData);
    }

    /*private void exportToPDF() {
        try {
            // Créer le FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le PDF");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );
            
            // Générer un nom de fichier par défaut avec la date
            String defaultFileName = "Users_List_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")) + ".pdf";
            fileChooser.setInitialFileName(defaultFileName);

            // Afficher la boîte de dialogue de sauvegarde
            File file = fileChooser.showSaveDialog(ExportPdf.getScene().getWindow());
            
            if (file != null) {
                // Créer le document PDF
                Document document = new Document(PageSize.A4, 50, 50, 50, 50);
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Ajouter le titre
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
                Paragraph title = new Paragraph("Liste des Utilisateurs", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                // Créer le tableau
                PdfPTable table = new PdfPTable(6); // 6 colonnes
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Définir les largeurs relatives des colonnes
                float[] columnWidths = {2f, 2.5f, 3f, 2f, 2f, 2.5f};
                table.setWidths(columnWidths);

                // Style pour les en-têtes
                Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
                BaseColor headerBackground = new BaseColor(12, 190, 184); // #0CBEB8

                // Ajouter les en-têtes
                String[] headers = {"Username", "Nom", "Email", "Téléphone", "Rôle", "Date création"};
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setBackgroundColor(headerBackground);
                    cell.setPadding(8);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }

                // Style pour le contenu
                Font contentFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
                BaseColor alternateColor = new BaseColor(240, 240, 240);

                // Récupérer les données
                ServiceProfile serviceProfile = new ServiceProfile();
                ServiceUser serviceUser = new ServiceUser();
                List<profile> profiles = serviceProfile.afficher();

                // Ajouter les données
                boolean alternate = false;
                for (profile p : profiles) {
                    user u = serviceUser.getUserById(p.getId_user());
                    
                    // Ajouter chaque cellule avec le style approprié
                    addCell(table, u.getUsername(), contentFont, alternate);
                    addCell(table, p.getName_u(), contentFont, alternate);
                    addCell(table, p.getEmail_u(), contentFont, alternate);
                    addCell(table, String.valueOf(p.getPhone_u()), contentFont, alternate);
                    addCell(table, p.getRole(), contentFont, alternate);
                    addCell(table, p.getCreated_at() != null ? p.getCreated_at().toString() : "", contentFont, alternate);
                    
                    alternate = !alternate;
                }

                document.add(table);

                // Ajouter la date d'export
                Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
                Paragraph footer = new Paragraph("Document exporté le : " + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), footerFont);
                footer.setAlignment(Element.ALIGN_RIGHT);
                footer.setSpacingBefore(20);
                document.add(footer);

                document.close();

                showAlert(Alert.AlertType.INFORMATION, "Succès", 
                    "Le fichier PDF a été généré avec succès !\nEmplacement : " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Une erreur est survenue lors de la génération du PDF : " + e.getMessage());
        }
    }

    private void addCell(PdfPTable table, String content, Font font, boolean alternate) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        if (alternate) {
            cell.setBackgroundColor(new BaseColor(240, 240, 240));
        }
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }*/
}
