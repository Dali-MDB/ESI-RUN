package transport.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transport.core.GestionnaireTransport;
import transport.ui.MainApp;

import java.io.IOException;

public class MainController {

    @FXML
    private Label statusLabel;

    private GestionnaireTransport gestionnaire;

    @FXML
    private void initialize() {
        gestionnaire = MainApp.getGestionnaireTransport();
        statusLabel.setText("Application prête.");
    }

    @FXML
    private void sauvegarderDonnees(ActionEvent event) {
        try {
            gestionnaire.sauvegarderDonnees();  //save the data
            statusLabel.setText("data saved successfully.");  //set the status label
        } catch (IOException e) {
            afficherErreur("error saving data", "impossible to save the data: " + e.getMessage());  //show the error message
        }
    }
    @FXML
    private void afficherListeEmployes(ActionEvent event) {  //open the list of employees window
        ouvrirFenetre("/views/ListeEmployesView.fxml", "Liste des employés");
    }
    @FXML
    private void restaurerDonnees(ActionEvent event) {  //restore the data
        try {
            GestionnaireTransport nouveauGestionnaire = GestionnaireTransport.chargerDonnees();  //load the data
            // Mettre à jour la référence globale
            MainApp.setGestionnaireTransport(nouveauGestionnaire);  //set the new gestionnaire
            this.gestionnaire = nouveauGestionnaire;  //set the new gestionnaire
            statusLabel.setText("Données restaurées avec succès.");
        } catch (Exception e) {
            afficherErreur("error restoring data", "impossible to restore the data: " + e.getMessage());  //show the error message
        }
    }

    @FXML
    private void quitterApplication(ActionEvent event) {
        // Ask for confirmation before quitting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); //create a confirmation alert
        alert.setTitle("Confirmation"); //set the title of the alert
        alert.setHeaderText("quit the application"); //set the header text of the alert
        alert.setContentText("do you want to save the data before quitting?"); //set the content text of the alert

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    gestionnaire.sauvegarderDonnees();  //save the data
                } catch (IOException e) {
                    afficherErreur("error saving data", "impossible to save the data: " + e.getMessage());  //show the error message
                }
            }
            //close the application
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.close();
        });
    }

    // Methods to display different views
    @FXML
    private void afficherAjoutUsager(ActionEvent event) {
        ouvrirFenetre("/views/AjoutUsagerView.fxml", "Ajouter un usager");
    }

    @FXML
    private void afficherAjoutEmploye(ActionEvent event) {  //open the add employee window
        ouvrirFenetre("/views/AjoutEmployeView.fxml", "Ajouter un employé");
    }

    @FXML
    private void afficherVenteTitre(ActionEvent event) {  //open the sell title window
        ouvrirFenetre("/views/VenteTitreView.fxml", "Vendre un titre de transport");
    }

    @FXML
    private void afficherValidationTitre(ActionEvent event) {  //open the validate title window
        ouvrirFenetre("/views/ValidationTitreView.fxml", "Valider un titre de transport");
    }

    @FXML
    private void afficherListeUsagers(ActionEvent event) {  //open the list of usagers window
        ouvrirFenetre("/views/ListeUsagersView.fxml", "Liste des usagers");
    }

    @FXML
    private void afficherListeTitres(ActionEvent event) {  //open the list of titles window
        ouvrirFenetre("/views/ListeTitresView.fxml", "Liste des titres");
    }

    @FXML
    private void afficherAjoutReclamation(ActionEvent event) {  //open the add complaint window
        ouvrirFenetre("/views/AjoutReclamationView.fxml", "Ajouter une réclamation");
    }

    @FXML
    private void afficherListeReclamations(ActionEvent event) {  //open the list of complaints window
        ouvrirFenetre("/views/ListeReclamationsView.fxml", "Liste des réclamations");
    }

    @FXML
    private void afficherAPropos(ActionEvent event) {  //open the about window
        Alert alert = new Alert(Alert.AlertType.INFORMATION);  //create an information alert
        alert.setTitle("À propos");  //set the title of the alert
        alert.setHeaderText("ESI-RUN - Système de gestion de transport");  //set the header text of the alert
        alert.setContentText("Version 1.0\nDéveloppé dans le cadre du TP POO 2024/2025\nEcole Supérieure d'Informatique (ESI-Alger)");
        alert.showAndWait();
    }

    // Utility method to open a new window
    private void ouvrirFenetre(String fxmlPath, String titre) {  //open a new window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));  //load the fxml file
            Parent root = loader.load();  //load the root of the fxml file
            Stage stage = new Stage();  //create a new stage
            stage.setTitle(titre);  //set the title of the stage
            stage.setScene(new Scene(root));  //set the scene of the stage
            stage.initModality(Modality.APPLICATION_MODAL);  //set the modality of the stage
            stage.show();  //show the stage
        } catch (IOException e) {
            afficherErreur("error opening window", "impossible to open the window: " + e.getMessage());  //show the error message
        }
    }

    // Utility method to display an error
    private void afficherErreur(String titre, String message) {  //show the error message
        Alert alert = new Alert(Alert.AlertType.ERROR);  //create an error alert
        alert.setTitle(titre);  //set the title of the alert
        alert.setHeaderText(null);  //set the header text of the alert
        alert.setContentText(message);  //set the content text of the alert
        alert.showAndWait();  //show the alert
    }
}