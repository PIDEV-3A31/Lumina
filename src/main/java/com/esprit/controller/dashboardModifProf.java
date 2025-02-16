package com.esprit.controller;

import com.esprit.models.profile;
import com.esprit.models.user;
import com.esprit.services.ServiceProfile;
import com.esprit.services.ServiceUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class dashboardModifProf {
    private user connectedUser;
    private profile userProfile;
    private profile selectedProfile;
    private boolean isAddMode = false;
    private boolean isCurrentUser = false;

    @FXML
    private Label name_current_user;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField modifusername;
    @FXML
    private TextField modifemail;
    @FXML
    private PasswordField modifpassword;
    @FXML
    private TextField modifname;
    @FXML
    private TextField modifphone;
    @FXML
    private ComboBox<String> modifrole;
    @FXML
    private Button Save;
    @FXML
    private Label identfLabel;
    @FXML
    private ImageView image;
    @FXML
    private Button uploadImg;
    @FXML
    private Button retour;
    @FXML
    private Button deconnexion;
    @FXML
    private Button affich_mdp;
    @FXML
    private ImageView eye_icon;
    @FXML
    private ImageView img_current_user;
    
    private String selectedImagePath = null;
    private boolean isPasswordVisible = false;

    @FXML
    public void initialize() {
        Save.setOnAction(event -> Update());
        uploadImg.setOnAction(event -> ImageUpload());
        retour.setOnAction(event -> navigateBack());
        deconnexion.setOnAction(event -> logout());
        
        // Ajouter le gestionnaire pour l'affichage du mot de passe
        affich_mdp.setOnAction(event -> togglePasswordVisibility(modifpassword, affich_mdp, eye_icon, isPasswordVisible));
        name_current_user.setOnMouseClicked(event -> editCurrentUserProfile());

        name_current_user.setStyle("-fx-cursor: hand;");

    }

    public void initData(user connectedUser, profile userProfile, profile selectedProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        this.selectedProfile = selectedProfile;
        this.isAddMode = false;

        name_current_user.setText(userProfile.getName_u());

        ServiceUser serviceUser = new ServiceUser();
        user selectedUser = serviceUser.getUserById(selectedProfile.getId_user());

        modifusername.setText(selectedUser.getUsername());
        modifpassword.setText(selectedUser.getPassword());
        modifname.setText(selectedProfile.getName_u());
        modifemail.setText(selectedProfile.getEmail_u());
        modifphone.setText(String.valueOf(selectedProfile.getPhone_u()));
        modifrole.setValue(selectedProfile.getRole());
        Save.setText("Save Changes");
        identfLabel.setText("Modify Account Details");

        if (selectedProfile.getImage_u() != null) {
            Image img = new Image(getClass().getResource("/" + selectedProfile.getImage_u()).toExternalForm());
            image.setImage(img);
        }
    }

    public void initDataForAdd(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        this.isAddMode = true;

        name_current_user.setText(userProfile.getName_u());

        // Vider tous les champs pour l'ajout
        modifusername.setText("");
        modifpassword.setText("");
        modifname.setText("");
        modifemail.setText("");
        modifphone.setText("");
        modifrole.setValue(null);

        // Changer le texte du bouton
        Save.setText("Add User");
        identfLabel.setText("Add New Account");

    }

    public void initDataForCurrentUser(user connectedUser, profile userProfile) {
        this.connectedUser = connectedUser;
        this.userProfile = userProfile;
        this.selectedProfile = userProfile; // L'utilisateur connecté est l'utilisateur à modifier
        this.isAddMode = false;
        this.isCurrentUser = true;

        name_current_user.setText(userProfile.getName_u());

        modifusername.setText(connectedUser.getUsername());
        modifpassword.setText(connectedUser.getPassword());
        modifname.setText(userProfile.getName_u());
        modifemail.setText(userProfile.getEmail_u());
        modifphone.setText(String.valueOf(userProfile.getPhone_u()));
        modifrole.setValue(userProfile.getRole());
        
        // Désactiver la modification du rôle pour l'utilisateur connecté
        modifrole.setDisable(true);
        
        Save.setText("Save My Profile");
        identfLabel.setText("Edit My Profile");

        if (selectedProfile.getImage_u() != null) {
            Image img = new Image(getClass().getResource("/" + selectedProfile.getImage_u()).toExternalForm());
            image.setImage(img);
        }

        // Masquer les champs de mot de passe pour l'utilisateur connecté
        modifpassword.setVisible(false);
        modifpassword.setManaged(false);
        affich_mdp.setVisible(false);
        affich_mdp.setManaged(false);
        // Masquer aussi le label du mot de passe si vous en avez un
        passwordLabel.setVisible(false);
        passwordLabel.setManaged(false);
    }
    public void initData(user user, profile profile) {
        this.connectedUser = user;
        this.userProfile = profile;
        updateUI();
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

    private void ImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Créer un nom unique pour l'image
                String uniqueFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path destinationPath = Paths.get("src/main/resources/uploads", uniqueFileName);
                
                // Créer le dossier uploads s'il n'existe pas
                Files.createDirectories(destinationPath.getParent());
                
                // Copier l'image vers le dossier uploads
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                
                // Sauvegarder le chemin relatif
                selectedImagePath = "uploads/" + uniqueFileName;
                
                // Afficher l'image dans l'ImageView
                Image img = new Image(selectedFile.toURI().toString());
                image.setImage(img);
                
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
    }

    private void Update() {
        try {
            if (!validateInputs()) {
                return;
            }

            if (isAddMode) {
                user newUser = new user(modifusername.getText(), modifpassword.getText());
                ServiceUser serviceUser = new ServiceUser();
                int userId = serviceUser.ajouterAvecId(newUser);

                if (userId == -1) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la création de l'utilisateur!");
                    return;
                }

                profile newProfile = new profile(
                    userId,
                    modifname.getText(),
                    modifemail.getText(),
                    Integer.parseInt(modifphone.getText()),
                    modifrole.getValue(),
                    selectedImagePath
                );
                ServiceProfile serviceProfile = new ServiceProfile();
                serviceProfile.ajouter(newProfile);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté avec succès!");
            } else {
                user updatedUser = new user(selectedProfile.getId_user(),
                    modifusername.getText(), modifpassword.getText());
                ServiceUser serviceUser = new ServiceUser();
                serviceUser.modifer(updatedUser, selectedProfile.getId_user());

                profile updatedProfile = new profile(
                    selectedProfile.getId_user(),
                    selectedProfile.getId_profile(),
                    modifname.getText(),
                    modifemail.getText(),
                    Integer.parseInt(modifphone.getText()),
                    modifrole.getValue(),
                    selectedImagePath != null ? selectedImagePath : selectedProfile.getImage_u()
                );
                ServiceProfile serviceProfile = new ServiceProfile();
                serviceProfile.modifer(updatedProfile, selectedProfile.getId_profile());

                if (isCurrentUser) {
                    this.connectedUser = updatedUser;
                    this.userProfile = updatedProfile;
                }

                showAlert(Alert.AlertType.INFORMATION, "Succès", 
                    isCurrentUser ? "Votre profil a été mis à jour avec succès!" : "Modifications enregistrées avec succès!");
            }
            
            // Utiliser la nouvelle méthode de navigation
            navigateAfterAction();
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la " + (isAddMode ? "l'ajout" : "modification") + ": " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        // Vérification des champs vides
        if (modifusername.getText().isEmpty() || modifemail.getText().isEmpty() || 
            modifpassword.getText().isEmpty() || modifname.getText().isEmpty() || 
            modifphone.getText().isEmpty() || modifrole.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs!");
            return false;
        }

        // Vérification de la longueur du username
        if (modifusername.getText().length() > 20) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Username must not exceed 20 characters!");
            return false;
        }

        // Vérification de la longueur du nom
        if (modifname.getText().length() > 25) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Name must not exceed 25 characters!");
            return false;
        }

        // Vérification du numéro de téléphone (8 chiffres)
        if (!modifphone.getText().matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Phone number must be exactly 8 digits!");
            return false;
        }

        // Validation de l'email
        if (!modifemail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Please enter a valid email address!");
            return false;
        }

        return true;
    }

    private void navigateAfterAction() {
        try {
            FXMLLoader loader;
            if (isCurrentUser) {
                loader = new FXMLLoader(getClass().getResource("/dashboardAffichProf.fxml"));
                Parent root = loader.load();
                dashboardAffichProf controller = loader.getController();
                controller.initData(connectedUser, userProfile);
            } else {
                loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
                Parent root = loader.load();
                dashboardController controller = loader.getController();
                controller.initData(connectedUser, userProfile);
            }

            Stage stage = (Stage) Save.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
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

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void navigateBack() {
        try {
            FXMLLoader loader;
            Parent root = null;

            if (isCurrentUser) {
                loader = new FXMLLoader(getClass().getResource("/dashboardAffichProf.fxml"));
                root = loader.load();
                dashboardAffichProf controller = loader.getController();
                controller.initData(connectedUser, userProfile);
            } else {
                loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
                root = loader.load();
                dashboardController controller = loader.getController();
                controller.initData(connectedUser, userProfile);
            }

            Stage stage = (Stage) retour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation!");
        }
    }

    private void logout() {
        Stage currentStage = (Stage) deconnexion.getScene().getWindow();
        loginn.logout(currentStage);
    }

    /*private void togglePasswordVisibility() {
        if (!isPasswordVisible) {
            // Afficher le mot de passe
            String password = modifpassword.getText();
            modifpassword.setPromptText(modifpassword.getText());
            modifpassword.clear();
            modifpassword.setStyle("-fx-text-inner-color: black;");
            eye_icon.setImage(new Image(getClass().getResource("/assets/eye.png").toExternalForm()));
        } else {
            // Cacher le mot de passe
            String password = modifpassword.getPromptText();
            modifpassword.setText(password);
            modifpassword.setPromptText("");
            modifpassword.setStyle("");
            eye_icon.setImage(new Image(getClass().getResource("/assets/eye-slash.png").toExternalForm()));
        }
        isPasswordVisible = !isPasswordVisible;
    }*/

    private void togglePasswordVisibility(PasswordField passwordField, Button button, ImageView eyeIcon, boolean isVisible) {
        if (!isVisible) {
            // Afficher le mot de passe
            String password = passwordField.getText();
            passwordField.setPromptText(passwordField.getText());
            passwordField.clear();
            passwordField.setStyle("-fx-text-inner-color: black;");
            eyeIcon.setImage(new Image(getClass().getResource("/assets/eye.png").toExternalForm()));
        } else {
            // Cacher le mot de passe
            String password = passwordField.getPromptText();
            passwordField.setText(password);
            passwordField.setPromptText("");
            passwordField.setStyle("");
            eyeIcon.setImage(new Image(getClass().getResource("/assets/eye-slash.png").toExternalForm()));
        }

        // Inverser l'état
        if (passwordField == modifpassword) {
            isPasswordVisible = !isPasswordVisible;}
    }
}
