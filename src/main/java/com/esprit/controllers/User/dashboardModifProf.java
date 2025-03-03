package com.esprit.controllers.User;

import com.esprit.controllers.loginn;
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
    private profile selectedProfile = userProfile;
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
    @FXML
    private Label Alert_username;
    @FXML
    private Label Alert_email;
    @FXML
    private Label Alert_password;
    @FXML
    private Label Alert_name;
    @FXML
    private Label Alert_phone;
    @FXML
    private Label Alert_role;
    @FXML
    private Tooltip ttt;
    @FXML
    private Button tt;

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
        tt.setStyle("-fx-font: normal bold 12px Langdon; "
                + "-fx-base: #9E9B9A; "
                + "-fx-text-fill: gris;");
        ttt.setText("The password must be between 3 and 20 characters long and contain at least one uppercase letter.");
        tt.setTooltip(ttt);
    }

    public void initData(user connectedUser, profile userProfile, profile selectedProfile) throws Exception {
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
        updateUI();
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
        updateUI();

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
        updateUI();
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
    private void loadImage(String imagePath, ImageView imageView) {
        if (imagePath != null) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource("/" + imagePath)).toExternalForm());
                imageView.setImage(img);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        }
        updateUI();
    }

    private void ImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Copier l'image vers le dossier uploads
                Path sourcePath = selectedFile.toPath();
                Path targetPath = Paths.get("src/main/resources/uploads/" + selectedFile.getName());
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour l'affichage et sauvegarder le chemin
                selectedImagePath = "uploads/" + selectedFile.getName();
                Image img = new Image(targetPath.toUri().toString());
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
                // Création d'un nouvel utilisateur
                user newUser = new user(modifusername.getText(), modifpassword.getText());
                ServiceUser serviceUser = new ServiceUser();
                int userId = serviceUser.ajouterAvecId(newUser);
                
                if (userId == -1) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la création de l'utilisateur!");
                    return;
                }

                // Création du profil associé
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

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur créé avec succès!");
                
            } else {
                // Mise à jour d'un utilisateur existant
                ServiceUser serviceUser = new ServiceUser();
                if (isCurrentUser) {
                    connectedUser.setUsername(modifusername.getText());
                    serviceUser.modifer(connectedUser, connectedUser.getId());
                } else {
                    user selectedUser = serviceUser.getUserById(selectedProfile.getId_user());
                    selectedUser.setUsername(modifusername.getText());
                    selectedUser.setPassword(modifpassword.getText());
                    serviceUser.modifer(selectedUser, selectedUser.getId());
                }

                // Mise à jour du profil
                profile profileToUpdate = isCurrentUser ? userProfile : selectedProfile;
                profileToUpdate.setName_u(modifname.getText());
                profileToUpdate.setEmail_u(modifemail.getText());
                profileToUpdate.setPhone_u(Integer.parseInt(modifphone.getText()));
                
                if (selectedImagePath != null) {
                    profileToUpdate.setImage_u(selectedImagePath);
                }

                if (!isCurrentUser) {
                    profileToUpdate.setRole(modifrole.getValue());
                }

                ServiceProfile serviceProfile = new ServiceProfile();
                serviceProfile.modifer(profileToUpdate, profileToUpdate.getId_profile());
            }

            navigateAfterAction();
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateAllImages(String imagePath) {
        // Mettre à jour l'image dans la vue actuelle
        loadImage(imagePath, img_current_user);
        loadImage(imagePath, image);

        // Mettre à jour l'image dans le tableau principal si nécessaire
        if (!isCurrentUser && selectedProfile != null) {
            selectedProfile.setImage_u(imagePath);
        }
    }

    private boolean validateInputs() {
        ServiceUser serviceUser = new ServiceUser();
        ServiceProfile serviceProfile = new ServiceProfile();
        boolean isValid = true;
        
        // Réinitialiser tous les messages d'erreur
        Alert_username.setText("");
        Alert_email.setText("");
        Alert_password.setText("");
        Alert_name.setText("");
        Alert_phone.setText("");
        Alert_role.setText("");

        // Vérification des champs vides
        /*if (modifusername.getText().isEmpty()) {
            Alert_username.setText("Username is required!");
            isValid = false;
        }*/

        /*if (modifemail.getText().isEmpty()) {
            Alert_email.setText("Email is required!");
            isValid = false;
        }*/

        /*if ((!isCurrentUser || isAddMode) && modifpassword.getText().isEmpty()) {
            Alert_password.setText("Password is required!");
            isValid = false;
        }*/

        if (modifname.getText().isEmpty() || modifname.getText().length() < 2 || modifname.getText().length() > 25) {
            if(modifname.getText().isEmpty())
                Alert_name.setText("Name is required!");
            else
                Alert_name.setText("Name must be between 2 and 25 characters!");
            isValid = false;
        }

        /*if (modifphone.getText().isEmpty()) {
            Alert_phone.setText("Phone is required!");
            isValid = false;
        }*/

        if (modifrole.getValue() == null) {
            Alert_role.setText("Role is required!");
            isValid = false;
        }

        // Vérification de l'unicité du username
        Integer excludeUserId = null;
        Integer excludeProfileId = null;
        if (!isAddMode) {
            excludeUserId = isCurrentUser ? connectedUser.getId() : selectedProfile.getId_user();
            excludeProfileId = isCurrentUser ? userProfile.getId_profile() : selectedProfile.getId_profile();
        }
        
        if (!serviceUser.isUsernameUnique(modifusername.getText(), excludeUserId) || modifusername.getText().isEmpty()) {
            if(modifusername.getText().isEmpty())
                Alert_username.setText("Username is required!");
            else
                Alert_username.setText("Username already exists!");
            isValid = false;
        }

        // Vérification de l'unicité de l'email
        if (!serviceProfile.isEmailUnique(modifemail.getText(), excludeProfileId)) {
            Alert_email.setText("Email already exists!");
            isValid = false;
        }

        // Validation du mot de passe
        if ((!isCurrentUser || isAddMode) && !serviceUser.isValidPassword(modifpassword.getText()) || (!isCurrentUser || isAddMode) && modifpassword.getText().isEmpty()) {
            if((!isCurrentUser || isAddMode) && modifpassword.getText().isEmpty())
                Alert_password.setText("Password is required!");
            else
                Alert_password.setText("Password must be between 3-20 characters with at least one uppercase!");
            isValid = false;
        }

        // Validation du numéro de téléphone
        if (!modifphone.getText().matches("^[5294]\\d{7}$") || modifphone.getText().isEmpty()) {
            if(modifphone.getText().isEmpty())
                Alert_phone.setText("Phone is required!");
            else
                Alert_phone.setText("Phone must start with 5, 2, 9, or 4 and have 8 digits!");
            isValid = false;
        }

        // Validation de l'email
        if (!modifemail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$") || modifemail.getText().isEmpty()) {
            if(modifemail.getText().isEmpty())
                Alert_email.setText("Email is required!");
            else
                Alert_email.setText("Please enter a valid email address!");
            isValid = false;
        }

        return isValid;
    }

    private void navigateAfterAction() {
        try {
            FXMLLoader loader;
            if (isCurrentUser) {
                loader = new FXMLLoader(getClass().getResource("/dashboardAffichProf.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            }

            Parent root = loader.load();
            Object controller = loader.getController();

            if (controller instanceof dashboardAffichProf) {
                ((dashboardAffichProf) controller).initData(connectedUser, userProfile);
            } else if (controller instanceof dashboardController) {
                dashboardController dashController = (dashboardController) controller;
                dashController.initData(connectedUser, userProfile);
                dashController.refreshTableView(); // Forcer le rafraîchissement
            }

            Stage stage = (Stage) Save.getScene().getWindow();
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
