package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transport.core.GestionnaireTransport;
import transport.ui.MainApp;

import java.io.IOException;

public class MainController {

    @FXML
    private Label statusLabel;

    @FXML
    private VBox centerContent;

    private GestionnaireTransport gestionnaire;

    @FXML
    private void initialize() {
        gestionnaire = MainApp.getGestionnaireTransport();
        statusLabel.setText("Application prête.");
    }

    @FXML
    private void sauvegarderDonnees(ActionEvent event) {
        try {
            gestionnaire.sauvegarderDonnees();
            statusLabel.setText("Données sauvegardées avec succès.");
        } catch (IOException e) {
            afficherErreur("Erreur de sauvegarde", "Impossible de sauvegarder les données: " + e.getMessage());
        }
    }

    @FXML
    private void afficherListeEmployes(ActionEvent event) {
        chargerVue("/views/ListeEmployesView.fxml");
    }

    @FXML
    private void restaurerDonnees(ActionEvent event) {
        try {
            GestionnaireTransport nouveauGestionnaire = GestionnaireTransport.chargerDonnees();
            MainApp.setGestionnaireTransport(nouveauGestionnaire);
            this.gestionnaire = nouveauGestionnaire;
            statusLabel.setText("Données restaurées avec succès.");
        } catch (Exception e) {
            afficherErreur("Erreur de restauration", "Impossible de restaurer les données: " + e.getMessage());
        }
    }

    @FXML
    private void quitterApplication(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Quitter l'application");
        alert.setContentText("Voulez-vous sauvegarder les données avant de quitter?");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    gestionnaire.sauvegarderDonnees();
                } catch (IOException e) {
                    afficherErreur("Erreur de sauvegarde", "Impossible de sauvegarder les données: " + e.getMessage());
                }
            }
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    private void afficherAjoutUsager(ActionEvent event) {
        chargerVue("/views/AjoutUsagerView.fxml");
    }

    @FXML
    private void afficherAjoutEmploye(ActionEvent event) {
        chargerVue("/views/AjoutEmployeView.fxml");
    }

    @FXML
    private void afficherVenteTitre(ActionEvent event) {
        chargerVue("/views/VenteTitreView.fxml");
    }

    @FXML
    private void afficherValidationTitre(ActionEvent event) {
        chargerVue("/views/ValidationTitreView.fxml");
    }

    @FXML
    private void afficherListeUsagers(ActionEvent event) {
        chargerVue("/views/ListeUsagersView.fxml");
    }

    @FXML
    private void afficherListeTitres(ActionEvent event) {
        chargerVue("/views/ListeTitresView.fxml");
    }

    @FXML
    private void afficherAjoutReclamation(ActionEvent event) {
        chargerVue("/views/AjoutReclamationView.fxml");
    }

    @FXML
    private void afficherListeReclamations(ActionEvent event) {
        chargerVue("/views/ListeReclamationsView.fxml");
    }

    @FXML
    private void afficherAPropos(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("ESI-RUN - Système de gestion de transport");
        alert.setContentText(
                "Version 1.0\nDéveloppé dans le cadre du TP POO 2024/2025\nEcole Supérieure d'Informatique (ESI-Alger)");
        alert.showAndWait();
    }

    private void chargerVue(String fxmlPath) {
        try {
            // Load the new view
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Get the controller of the loaded view
            Object controller = loader.getController();

            // If the controller has a setGestionnaire method, set the gestionnaire
            try {
                controller.getClass().getMethod("setGestionnaire", GestionnaireTransport.class)
                        .invoke(controller, gestionnaire);
            } catch (Exception e) {
                // Ignore if the controller doesn't have setGestionnaire method
            }

            // Clear the center content and add the new view
            centerContent.getChildren().clear();
            centerContent.getChildren().add(root);

            // Update status
            String viewName = fxmlPath.substring(fxmlPath.lastIndexOf("/") + 1, fxmlPath.lastIndexOf("."));
            statusLabel.setText("Vue actuelle: " + viewName);

        } catch (IOException e) {
            afficherErreur("Erreur d'interface", "Impossible de charger la vue: " + e.getMessage());
        }
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}